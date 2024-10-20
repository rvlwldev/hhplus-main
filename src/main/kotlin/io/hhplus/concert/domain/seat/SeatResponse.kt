package io.hhplus.concert.domain.seat

class SeatResponse(
    val seatNumber: Int,
    val status: String
) {
    constructor(seat: Seat) : this(
        seatNumber = seat.seatNumber,
        status = seat.status.name
    )
}
