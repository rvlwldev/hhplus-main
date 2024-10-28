package io.hhplus.concert.domain.seat

class SeatInfo(
    val scheduleId: Long,
    val seatNumber: Long,
    val status: String,
    val isReservable: Boolean
) {
    constructor(seat: Seat) : this(
        scheduleId = seat.scheduleId,
        seatNumber = seat.seatNumber,
        status = seat.status.name,
        isReservable = seat.status === SeatStatus.EMPTY
    )
}
