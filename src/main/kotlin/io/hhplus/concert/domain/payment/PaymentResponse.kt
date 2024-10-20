package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.seat.Seat
import java.time.LocalDateTime

data class PaymentResponse(
    val concertId: Long,
    val concertName: String,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
    val seatNumber: Int,
    val amount: Long,
    val paidAt: LocalDateTime
) {
    constructor(seat: Seat, payment: Payment) : this(
        concertId = seat.schedule.concert.id,
        concertName = seat.schedule.concert.name,
        sttAt = seat.schedule.sttAt,
        endAt = seat.schedule.endAt,
        seatNumber = seat.seatNumber,
        amount = payment.amount,
        paidAt = payment.paidAt
    )
}
