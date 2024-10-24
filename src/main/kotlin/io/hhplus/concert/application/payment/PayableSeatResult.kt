package io.hhplus.concert.application.payment

import io.hhplus.concert.domain.seat.SeatInfo

data class PayableSeatResult(
    val concertId: Long,
    val scheduleId: Long,
    val seatNumber: Long
) {
    constructor(info: SeatInfo) : this(
        concertId = info.concertId,
        scheduleId = info.scheduleId,
        seatNumber = info.seatNumber,
    )
}