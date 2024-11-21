package io.hhplus.concert.domain.seat.event

import io.hhplus.concert.domain.seat.Seat

class SeatEvent {
    data class Init(
        val id: Long = 0L,
        val scheduleId: Long,
        val userId: Long,
        val seatNumber: Long,
        val status: String
    ) {
        constructor(seat: Seat) : this(
            id = seat.id,
            scheduleId = seat.scheduleId,
            userId = seat.userId!!,
            seatNumber = seat.seatNumber,
            status = seat.status.toString()
        )
    }

    data class Confirm(
        val id: Long = 0L,
        val scheduleId: Long,
        val userId: Long,
        val seatNumber: Long,
        val status: String
    ) {
        constructor(seat: Seat) : this(
            id = seat.id,
            scheduleId = seat.scheduleId,
            userId = seat.userId!!,
            seatNumber = seat.seatNumber,
            status = seat.status.toString()
        )
    }
}



