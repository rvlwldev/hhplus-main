package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.payment.result.PayableSeatResult

data class PayableSeatResponse(
    val concertId: Long,
    val scheduleId: Long,
    val seatNumber: Long
) {
    constructor(result: PayableSeatResult) : this(
        concertId = result.concertId,
        scheduleId = result.scheduleId,
        seatNumber = result.seatNumber,
    )
}