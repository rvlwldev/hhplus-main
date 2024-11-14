package io.hhplus.concert.domain.schedule

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.domain.seat.SeatStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ScheduleService(
    private val repo: ScheduleRepository,
    private val seatRepo: SeatRepository,
) {

    // TODO :  시간 정합성 체크
    @Transactional
    fun create(
        concertId: Long,
        maximumReservableCount: Long,
        sttAt: LocalDateTime,
        endAt: LocalDateTime,
        sttReserveAt: LocalDateTime,
        endReserveAt: LocalDateTime
    ) = repo.save(
        Schedule(
            concertId = concertId,
            maximumReservableCount = maximumReservableCount,
            reservableCount = maximumReservableCount,
            sttAt = sttAt,
            endAt = endAt,
            sttReserveAt = sttReserveAt,
            endReserveAt = endReserveAt
        )
    ).run { ScheduleInfo(this) }

    fun get(id: Long) = repo.findById(id)
        ?.run { ScheduleInfo(this) }
        ?: throw BizException(BizError.Schedule.NOT_FOUND)

    fun getAll(concertId: Long) = repo.findAllByConcertId(concertId)
        .map { ScheduleInfo(it) }

    fun getReservableList(id: Long): List<Long> {
        val schedule = repo.findById(id)
            ?: throw BizException(BizError.Schedule.NOT_FOUND)

        val seatNumbers = seatRepo.findAllByScheduleId(id)
            .filter { it.status == SeatStatus.EMPTY }
            .map { it.seatNumber }

        return (1..schedule.reservableCount)
            .filter { !seatNumbers.contains(it.toLong()) }
            .map { it.toLong() }
    }

    fun isReservable(id: Long) = repo.findById(id)
        ?.run { this.reservableCount > 0 }
        ?: throw BizException(BizError.Schedule.NOT_FOUND)

}