package concert.unit

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.repository.ConcertRepository
import io.hhplus.concert.domain.concert.repository.SeatRepository
import io.hhplus.concert.domain.concert.entity.ConcertSchedule
import io.hhplus.concert.domain.concert.entity.Seat
import io.hhplus.concert.domain.concert.entity.SeatStatus
import io.hhplus.concert.domain.concert.ConcertService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.kotlin.any
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import kotlin.test.Test

class ConcertServiceTest {
    private val repo: ConcertRepository = mock(ConcertRepository::class.java)
    private val seatRepo: SeatRepository = mock(SeatRepository::class.java)
    private val service = ConcertService(repo, seatRepo)

    @Test
    fun `없는 좌석 예약 시 예외`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val seatId = 1L

        `when`(seatRepo.findWithLock(scheduleId, seatId)).thenReturn(null)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.reserveSeat(userId, scheduleId, seatId)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.code)
        assertEquals("해당 좌석을 찾을 수 없습니다.", exception.message)
    }

    @Test
    fun `좌석이 반 자리가 아닐 떄 예외`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val seatId = 1L
        val seat = Seat(seatId = seatId, concertSchedule = ConcertSchedule(), userId = 0L, status = SeatStatus.RESERVED)

        `when`(seatRepo.findWithLock(scheduleId, seatId)).thenReturn(seat)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.reserveSeat(userId, scheduleId, seatId)
        }

        assertEquals(HttpStatus.CONFLICT, exception.code)
        assertEquals("이미 예약된 좌석입니다.", exception.message)
    }

    @Test
    fun `빈 자리일때만 성공`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val seatId = 1L
        val seat = Seat(seatId = seatId, concertSchedule = ConcertSchedule(), userId = 0L, status = SeatStatus.EMPTY)

        `when`(seatRepo.findWithLock(scheduleId, seatId)).thenReturn(seat)
        `when`(seatRepo.save(any())).thenReturn(seat)

        // When
        service.reserveSeat(userId, scheduleId, seatId)

        // Then
        assertEquals(userId, seat.userId)
        assertEquals(SeatStatus.WAIT, seat.status)
        verify(seatRepo).save(seat)
    }
}