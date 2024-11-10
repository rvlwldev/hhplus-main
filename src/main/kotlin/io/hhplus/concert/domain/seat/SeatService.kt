package io.hhplus.concert.domain.seat

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class SeatService(private val repo: SeatRepository) {

    fun get(scheduleId: Long, seatNumber: Long): SeatInfo? {
        return repo.findByScheduleIdAndNumber(scheduleId, seatNumber)
            ?.run { SeatInfo(this) }
    }

    fun getOrCreate(scheduleId: Long, seatNumber: Long): SeatInfo {
        var seat = repo.findByScheduleIdAndNumber(scheduleId, seatNumber)

        if (seat == null) {
            seat = Seat(scheduleId = scheduleId, seatNumber = seatNumber)
            repo.save(seat)
        }

        return SeatInfo(seat)
    }

    fun readyToReserve(id: Long, userId: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.readyToReserve(userId)

        return SeatInfo(repo.save(seat))
    }

    fun confirm(id: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.confirmReservation()

        return SeatInfo(repo.save(seat))
    }

    fun cancel(id: Long): SeatInfo {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.cancel()

        return SeatInfo(repo.save(seat))
    }
}