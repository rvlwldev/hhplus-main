package facade

import io.hhplus.concert.application.payment.PaymentFacade
import io.hhplus.concert.application.payment.result.PaymentResult
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.core.outbox.OutBox
import io.hhplus.concert.domain.core.outbox.OutBoxRepository
import io.hhplus.concert.domain.core.outbox.OutBoxService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatInfo
import io.hhplus.concert.domain.seat.SeatService
import io.hhplus.concert.domain.seat.SeatStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.kafka.core.KafkaTemplate

@ExtendWith(MockitoExtension::class)
class PaymentFacadeTest {

    @Mock
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Mock
    private lateinit var outBoxRepository: OutBoxRepository

    @InjectMocks
    private lateinit var outBoxService: OutBoxService

    @Mock
    private lateinit var seatService: SeatService

    private lateinit var paymentFacade: PaymentFacade

    @BeforeEach
    fun setup() {
        paymentFacade = PaymentFacade(
            concertService = mock(ConcertService::class.java),
            scheduleService = mock(ScheduleService::class.java),
            seatService = seatService,
            paymentService = mock(PaymentService::class.java)
        )
    }

    @Test
    fun `이벤트 발행 시 성공 여부 테스트`() {
        val seat = Seat(
            id = 1L,
            userId = 1L,
            scheduleId = 1L,
            seatNumber = 1L,
            status = SeatStatus.WAIT
        )

        whenever(seatService.get(any<Long>(), any<Long>())).then { SeatInfo(seat) }
        whenever(outBoxService.isDone(1L, "Seat")).thenReturn(false)
        whenever(outBoxRepository.save(any<OutBox>())).thenAnswer { invocation ->
            val argument = invocation.arguments[0] as OutBox
            argument
        }

        whenever(paymentFacade.pay(any<Long>(), any<Long>(), any<Long>()))
            .thenReturn(
                PaymentResult(
                    paymentId = 1L,
                    scheduleId = 1L,
                    seatNumber = 1L,
                    amount = 1L,
                    status = "WAIT"
                )
            )

        verify(kafkaTemplate, times(1)).send(eq("test-topic"), any<String>())
        verify(outBoxRepository, times(1)).save(any())
    }

    @Test
    fun `이벤트 발행 시 409 에러 테스트`() {
        whenever(outBoxService.isDone(1L, "Seat")).thenReturn(true)

        val exception = assertThrows<BizException> {
            paymentFacade.pay(1L, 1L, 1L)
        }

        assert(exception.message!!.contains("중복된 요청입니다."))
        verify(kafkaTemplate, times(0)).send(any(), any())
    }
}