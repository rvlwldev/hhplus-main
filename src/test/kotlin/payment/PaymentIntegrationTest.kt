package payment

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.application.component.TokenProvider
import io.hhplus.concert.domain.concert.entity.Concert
import io.hhplus.concert.domain.concert.entity.ConcertSchedule
import io.hhplus.concert.domain.concert.entity.Seat
import io.hhplus.concert.domain.concert.entity.SeatStatus
import io.hhplus.concert.domain.concert.repository.SeatRepository
import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.domain.queue.QueueStatus
import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.domain.user.entity.User
import io.hhplus.concert.infrastructure.concert.ConcertJpaRepository
import io.hhplus.concert.infrastructure.concert.ConcertScheduleJpaRepository
import io.hhplus.concert.infrastructure.concert.SeatJpaRepository
import io.hhplus.concert.infrastructure.user.UserPointHistoryJpaRepository
import io.hhplus.concert.presentation.payment.PaymentRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test

@SpringBootTest(classes = [ConcertApplication::class])
@AutoConfigureMockMvc
@Transactional
class PaymentIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository,
    val queueRepository: QueueRepository,
    val pointHistoryRepository: UserPointHistoryJpaRepository,
    val seatRepository: SeatRepository,
    val seatJpaRepository: SeatJpaRepository,
    val concertRepository: ConcertJpaRepository,
    val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    val tokenProvider: TokenProvider

) {


    @BeforeEach
    fun setup() {
        // 유저 생성
        val user = User(userId = 1L, point = 100000L)
        userRepository.save(user)

        // 콘서트 생성
        val concert = Concert(concertId = 1L, name = "Test Concert", price = 70000L)
        concertRepository.save(concert)

        // 스케줄 생성
        val schedule = ConcertSchedule(scheduleId = 1L, concert = concert)
        concertScheduleJpaRepository.save(schedule)

        // 좌석 생성
        val seat = Seat(seatId = 1L, concertSchedule = schedule, status = SeatStatus.EMPTY)
        seatRepository.save(seat)

        // 대기열 통과 상태 생성
        val queue = Queue(userId = user.userId, scheduleId = schedule.scheduleId, status = QueueStatus.PASS)
        queueRepository.save(queue)
    }

    @Test
    fun `Concurrent payment requests result in one failure`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val seatId = 1L

        // Create the payment token
        val payToken = tokenProvider.createPaymentToken(userId, scheduleId)

        // Prepare the payment request
        val paymentRequest = PaymentRequest(seatId = seatId)
        val requestContent = objectMapper.writeValueAsString(paymentRequest)

        // We'll use an executor service to run two threads concurrently
        val executorService = Executors.newFixedThreadPool(2)
        val latch = CountDownLatch(2)

        // AtomicInteger to count successful payments
        val successCount = AtomicInteger(0)

        // When
        for (i in 1..2) {
            executorService.submit {
                try {
                    val result = mockMvc.perform(
                        post("/payments")
                            .header("Authorization", payToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent)
                    ).andReturn()

                    val status = result.response.status

                    if (status == 200) {
                        successCount.incrementAndGet()
                    } else {
                        println("Request failed with status: $status")
                    }
                } catch (e: Exception) {
                    // Log the exception if necessary
                    println("Exception occurred: ${e.message}")
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // Then
        assertEquals(1, successCount.get())

        // 좌석 상태 확인
        val seat = seatJpaRepository.findById(seatId).get()
        assertEquals(SeatStatus.RESERVED, seat.status)
        assertEquals(userId, seat.userId)

        // 대기열 확인
        val queue = queueRepository.findByUserIdAndScheduleId(userId, scheduleId)
        assertEquals(queue, null)

        // 잔액 확인
        val user = userRepository.findById(userId)!!
        assertEquals(100000L - 70000L, user.point)


        // 결제
        val payments = pointHistoryRepository.findAllByUserId(userId)
        assertEquals(1, payments.size)
    }
}