package facade

import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.application.payment.PaymentFacade
import io.hhplus.concert.application.payment.result.PaymentResult
import io.hhplus.concert.application.support.RedisManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.Concert
import io.hhplus.concert.domain.concert.ConcertInfo
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.Payment
import io.hhplus.concert.domain.payment.PaymentInfo
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.payment.PaymentStatus
import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleInfo
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatInfo
import io.hhplus.concert.domain.seat.SeatService
import io.hhplus.concert.domain.seat.SeatStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(classes = [ConcertApplication::class])
class PaymentIntegrationTest {

    @Autowired
    private lateinit var paymentFacade: PaymentFacade

    @MockBean
    private lateinit var redisManager: RedisManager

    @MockBean
    private lateinit var concertService: ConcertService

    @MockBean
    private lateinit var scheduleService: ScheduleService

    @MockBean
    private lateinit var seatService: SeatService

    @MockBean
    private lateinit var paymentService: PaymentService

    private val userId: Long = 1L
    private val scheduleId: Long = 100L
    private val seatNumber: Long = 10L
    private val lockKey = "reservation:lock:$scheduleId:$seatNumber"

    @BeforeEach
    fun setup() {
        val concert = ConcertInfo(Concert(id = 1L, price = 5000))
        val schedule = ScheduleInfo(Schedule(id = scheduleId, concertId = concert.id, reservableCount = 50))
        val seat = SeatInfo(Seat(id = seatNumber, scheduleId = scheduleId, status = SeatStatus.EMPTY, seatNumber = seatNumber))
        val payment = PaymentInfo(Payment(id = 1L, userId = userId, status = PaymentStatus.WAIT))

        given(scheduleService.get(scheduleId)).willReturn(schedule)
        given(concertService.get(schedule.id)).willReturn(concert)
        given(seatService.get(scheduleId, seatNumber)).willReturn(seat)
        given(paymentService.getLatestByUserId(userId)).willReturn(payment)

    }

    @Test
    @Transactional
    fun `락이 없을때, 결제성공`() {
        val lockValue = "lockValue"
        given(redisManager.tryLock(lockKey)).willReturn(lockValue)

        val result: PaymentResult = paymentFacade.ready(userId, scheduleId, seatNumber)

        assertNotNull(result)
        assertEquals(scheduleId, result.scheduleId)
        assertEquals(seatNumber, result.seatNumber)

        Mockito.verify(redisManager).releaseLock(lockKey, lockValue)
    }

    @Test
    fun `락 획득 실패 시, ALREADY_IN_PROGRESS 예외 발생`() {
        given(redisManager.tryLock(lockKey)).willReturn(null)

        val exception = assertThrows<BizException> {
            paymentFacade.ready(userId, scheduleId, seatNumber)
        }

        assertEquals(BizError.Payment.ALREADY_IN_PROGRESS.second, exception.message)
    }

}
