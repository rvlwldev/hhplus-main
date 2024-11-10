package facade

import io.hhplus.concert.application.reservation.ReservationFacadeV2
import io.hhplus.concert.application.reservation.dto.ReservationResultV2
import io.hhplus.concert.application.support.RedisClient
import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertInfo
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentInfo
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleInfo
import io.hhplus.concert.domain.schedule.ScheduleService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime


@ExtendWith(MockitoExtension::class)
class ReservationFacadeV2Test {

    @Mock
    private lateinit var concertService: ConcertService

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var paymentService: PaymentService

    @Mock
    private lateinit var tokenManager: TokenManager

    @Mock
    private lateinit var queue: RedisClient

    @InjectMocks
    private lateinit var reservationFacade: ReservationFacadeV2

    @Test
    fun `reserve should throw connection error exception if reservation time is invalid`() {
        val userId = 123L
        val concertId = 1L
        val scheduleId = 1L

        val invalidSchedule = Schedule(
            id = scheduleId,
            concertId = concertId,
            sttReserveAt = LocalDateTime.now().plusDays(1),
            endReserveAt = LocalDateTime.now().plusDays(2),
            sttAt = LocalDateTime.now().plusDays(10),
            endAt = LocalDateTime.now().plusDays(10).plusHours(2),
            reservableCount = 50,
        )

        whenever(scheduleService.get(scheduleId)).thenReturn(ScheduleInfo(invalidSchedule))

        assertThrows<BizException> {
            reservationFacade.reserve(userId, scheduleId)
        }.also { exception ->
            assertEquals(BizError.Queue.CONNECTION_ERROR.second, exception.message)
        }
    }

    @Test
    fun `reserve should add user to queue and return ReservationResultV2`() {
        val userId = 123L
        val concertId = 1L
        val scheduleId = 1L

        val token = "test-token"

        whenever(scheduleService.get(scheduleId))
            .thenReturn(
                ScheduleInfo(
                    Schedule(
                        id = scheduleId,
                        concertId = concertId,
                        sttReserveAt = LocalDateTime.now().minusDays(2),
                        endReserveAt = LocalDateTime.now().plusDays(2),
                        sttAt = LocalDateTime.now().plusDays(10),
                        endAt = LocalDateTime.now().plusDays(10).plusHours(2),
                        reservableCount = 50,
                    )
                )
            )
        whenever(tokenManager.createQueueToken(userId, scheduleId))
            .thenReturn(token)
        whenever(queue.offer("WAIT::$scheduleId", userId))
            .thenReturn(true)
        whenever(queue.rank("WAIT::$scheduleId", userId))
            .thenReturn(10L)

        val result = reservationFacade.reserve(userId, scheduleId)

        assertEquals(ReservationResultV2(token, "WAIT", scheduleId, 10L), result)
    }

    @Test
    fun `getStatus should return 'WAIT' type if rank exceeds maximumPassCount`() {
        val token = "test-token"
        val claims = mapOf("userId" to 123L, "concertId" to 1L, "scheduleId" to 1L)
        val userId = 123L
        val scheduleId = 1L
        whenever(tokenManager.validateQueueToken(token)).thenReturn(claims)
        whenever(queue.rank("WAIT::$scheduleId", userId)).thenReturn(51L) // exceeding maximumPassCount

        val result = reservationFacade.getStatus(token)

        assertEquals("WAIT", result.type)
        assertEquals(scheduleId, result.scheduleId)
    }

    @Test
    fun `getStatus should create payment if rank is within maximumPassCount`() {
        val userId = 123L
        val concertId = 1L
        val scheduleId = 1L
        val price = 5000L

        val token = "queue-token"
        val newToken = "payment-token"
        val claims = mapOf("userId" to userId, "concertId" to concertId, "scheduleId" to scheduleId)
        val paymentId = 100L

        `when`(tokenManager.validateQueueToken(token)).thenReturn(claims)
        `when`(queue.rank("WAIT::$scheduleId", userId)).thenReturn(30L)

        val concertInfo = ConcertInfo(
            id = concertId,
            name = "test",
            price = price,
            maximumAudienceCount = 100L
        )
        `when`(concertService.get(concertId)).thenReturn(concertInfo)

        val paymentInfo = PaymentInfo(
            id = paymentId,
            amount = price,
            createdAt = LocalDateTime.now(),
            status = "READY",
            null
        )
        `when`(paymentService.create(userId, price)).thenReturn(paymentInfo)

        `when`(tokenManager.createPaymentToken(userId, scheduleId, paymentId)).thenReturn(newToken)

        val result = reservationFacade.getStatus(token)
        assertEquals("PASS", result.type)
        assertEquals(scheduleId, result.scheduleId)
        assertEquals(newToken, result.token)
    }



}
