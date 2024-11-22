package io.hhplus.concert.domain.seat

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.seat.event.SeatEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeatService(
    private val repo: SeatRepository,
    private val publisher: ApplicationEventPublisher
) {

    fun get(scheduleId: Long, seatNumber: Long): SeatInfo? {
        return repo.findByScheduleIdAndNumber(scheduleId, seatNumber)
            ?.run { SeatInfo(this) }
    }

    @Transactional
    fun getOrCreate(scheduleId: Long, seatNumber: Long): SeatInfo {
        var seat = repo.findByScheduleIdAndNumber(scheduleId, seatNumber)
        if (seat == null)
            seat = Seat(scheduleId = scheduleId, seatNumber = seatNumber)
        seat = repo.save(seat)

        publisher.publishEvent(SeatEvent.Init(seat))
        return SeatInfo(seat)
    }

    @Transactional
    fun readyToReserve(id: Long, userId: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.readyToReserve(userId)

        return SeatInfo(repo.save(seat))
    }

    @Transactional
    fun confirm(id: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.confirmReservation()

        publisher.publishEvent(SeatEvent.Confirm(seat))
        return SeatInfo(repo.save(seat))
    }

    fun cancel(id: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.cancel()

        return SeatInfo(repo.save(seat))
    }
}