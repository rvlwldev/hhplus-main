package io.hhplus.concert.domain.seat

class SeatInfo(
    val concertId: Long,
    val scheduleId: Long,
    val seatNumber: Long,
    val status: String
) {
    constructor(seat: Seat) : this(
        concertId = seat.schedule.concert.id,
        scheduleId = seat.schedule.id,
        seatNumber = seat.seatNumber,
        status = seat.status.name
    )
}
