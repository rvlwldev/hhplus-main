package concert.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.domain.concert.entity.Concert
import io.hhplus.concert.domain.concert.entity.ConcertSchedule
import io.hhplus.concert.domain.concert.entity.Seat
import io.hhplus.concert.domain.concert.entity.SeatStatus
import io.hhplus.concert.domain.user.entity.User
import io.hhplus.concert.infrastructure.concert.ConcertJpaRepository
import io.hhplus.concert.infrastructure.concert.ConcertScheduleJpaRepository
import io.hhplus.concert.infrastructure.concert.SeatJpaRepository
import io.hhplus.concert.infrastructure.user.UserJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest(classes = [ConcertApplication::class])
@AutoConfigureMockMvc
@Transactional
class ConcertControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val concertJpaRepository: ConcertJpaRepository,
    val concertScheduleJpaRepository: ConcertScheduleJpaRepository,
    val seatJpaRepository: SeatJpaRepository,
    val userJpaRepository: UserJpaRepository,
    val objectMapper: ObjectMapper
) {

    @BeforeEach
    fun setup() {
        seatJpaRepository.deleteAll()
        concertScheduleJpaRepository.deleteAll()
        concertJpaRepository.deleteAll()
        userJpaRepository.deleteAll()

        val concert = Concert(concertId = 10L, name = "Test Concert")
        concertJpaRepository.save(concert)

        val schedule = ConcertSchedule(
            scheduleId = 1L,
            concert = concert,
        )
        concertScheduleJpaRepository.save(schedule)

        val seat = Seat(
            seatId = 1L,
            concertSchedule = schedule,
            userId = null,
            status = SeatStatus.EMPTY
        )
        seatJpaRepository.save(seat)

        // 100명의 유저 생성
        val users = (1..100).map { userId ->
            User(userId = userId.toLong(), point = 1000L)
        }
        userJpaRepository.saveAll(users)
    }

    @Test
    fun `콘서트 조회 테스트`() {
        // When & Then
        val result = mockMvc.perform(get("/concerts"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val content = result.response.contentAsString
        val concerts = objectMapper.readTree(content)
        assertEquals(1, concerts.size())
        assertEquals("Test Concert", concerts[0]["name"].asText())
    }

    @Test
    fun `동시에 100명이 예약을 시도한다`() {
        val totalRequests = 100
        val successCount = AtomicInteger(0)
        val executorService = Executors.newFixedThreadPool(10)
        val latch = CountDownLatch(totalRequests)

        for (i in 1..totalRequests) {
            val userId = i.toLong()
            executorService.execute {
                try {
                    val response = mockMvc.perform(
                        put("/concerts/schedules/1/users/$userId/reserves")
                    ).andReturn()

                    if (response.response.status == 200) {
                        println("success: $i")
                        successCount.incrementAndGet()
                    }

                } catch (e: Exception) {
                    // 에러
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        println("성공한 예약 수: ${successCount.get()}")
        assertEquals(1, successCount.get(), "예약은 단 한 명만 성공해야 합니다.")
    }
}