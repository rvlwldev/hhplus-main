package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.payment.result.PaymentResult

data class PaymentResponse(
    val paymentId: Long,
    val scheduleId: Long,
    val seatNumber: Long,
    val amount: Long,
    val status: String,
) {
    constructor(result: PaymentResult) : this(
        paymentId = result.paymentId,
        scheduleId = result.scheduleId,
        seatNumber = result.seatNumber,
        amount = result.amount,
        status = result.status
    )
}