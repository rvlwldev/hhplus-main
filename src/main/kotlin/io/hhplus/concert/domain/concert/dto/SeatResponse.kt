package io.hhplus.concert.domain.concert.dto

import io.hhplus.concert.domain.concert.entity.Seat

class SeatResponse(
    val seatNumber: Int,
    val status: String
) {
    constructor(seat: Seat) : this(
        seatNumber = seat.seatNumber,
        status = seat.status.name
    )
}
