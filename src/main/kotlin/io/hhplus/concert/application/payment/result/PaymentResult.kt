package io.hhplus.concert.application.payment.result

import io.hhplus.concert.domain.payment.PaymentInfo


data class PaymentResult(
    val paymentId: Long,
    val scheduleId: Long,
    val seatNumber: Long,
    val amount: Long,
    val status: String
) {
    constructor(info: PaymentInfo, scheduleId: Long, seatNumber: Long) : this(
        paymentId = info.id,
        scheduleId = scheduleId,
        seatNumber = seatNumber,
        amount = info.amount,
        status = info.status
    )
}

