package facade

import io.hhplus.concert.application.payment.PaymentFacadeV2
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.ScheduleRepository
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatService
import io.hhplus.concert.domain.seat.SeatStatus
import io.hhplus.concert.infrastructure.implement.SeatRepositoryImpl
import io.hhplus.concert.infrastructure.jpa.SeatJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

@ExtendWith(MockitoExtension::class)
class PaymentFacadeV2Test {

    @Mock
    private lateinit var concertService: ConcertService

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var seatService: SeatService

    @Mock
    private lateinit var paymentService: PaymentService

    @Mock
    private lateinit var jpa: SeatJpaRepository

    @Mock
    private lateinit var listCache: RedisTemplate<String, List<Seat>>

    @Mock
    private lateinit var valueOperations: ValueOperations<String, List<Seat>>

    @Mock
    private lateinit var scheduleRepository: ScheduleRepository

    @InjectMocks
    private lateinit var seatRepository: SeatRepositoryImpl

    @InjectMocks
    private lateinit var paymentFacade: PaymentFacadeV2

    @Test
    fun `getPayableSeatList should return seats from cache if available`() {
        // given
        val scheduleId = 1L
        val cacheKey = "SEATS::$scheduleId"
        val cachedSeatList = listOf(
            Seat(scheduleId = scheduleId, seatNumber = 1L, status = SeatStatus.EMPTY),
            Seat(scheduleId = scheduleId, seatNumber = 2L, status = SeatStatus.EMPTY)
        )
        val expectedSeatNumbers = cachedSeatList.map { it.seatNumber }

        // RedisTemplate의 opsForValue 설정
        whenever(listCache.opsForValue()).thenReturn(valueOperations)
        // 캐시가 있다고 가정
        whenever(valueOperations.get(cacheKey)).thenReturn(cachedSeatList)

        // 캐시가 있을 때 호출되는지 검증
        val result = paymentFacade.getPayableSeatList(scheduleId)

        // 결과 검증
        assertEquals(expectedSeatNumbers, result)

        // 캐시가 조회되었는지 확인
        verify(valueOperations).get(cacheKey)
        verify(jpa, Mockito.never()).findAllByScheduleId(scheduleId)
    }
}
