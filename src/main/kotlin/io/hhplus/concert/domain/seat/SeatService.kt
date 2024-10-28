package io.hhplus.concert.domain.seat

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class SeatService(private val repo: SeatRepository) {

    fun getOrCreate(scheduleId: Long, seatNumber: Long): Seat {
        var seat = repo.findByScheduleIdAndNumber(scheduleId, seatNumber)

        if (seat == null) {
            seat = Seat(scheduleId = scheduleId, seatNumber = seatNumber)
            repo.save(seat)
        }

        return seat
    }

    fun readyToReserve(id: Long, userId: Long): Seat {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.readyToReserve(userId)

        return repo.save(seat)
    }

    fun confirm(id: Long): Seat {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.confirmReservation()

        return repo.save(seat)
    }

    fun cancel(id: Long): Seat {
        val seat = repo.findById(id)
            ?: throw BizException(BizError.Seat.NOT_FOUND)

        seat.cancel()

        return repo.save(seat)
    }
}