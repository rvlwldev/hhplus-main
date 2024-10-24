package io.hhplus.concert.domain.payment

import java.time.LocalDateTime

data class PaymentInfo(
    val paymentId: Long,
    val amount: Long,
    val createdAt: LocalDateTime,
    val paidAt: LocalDateTime?,
    val status: String,
    val updatedAt: LocalDateTime?
) {
    constructor(payment: Payment) : this(
        paymentId = payment.id,
        amount = payment.amount,
        createdAt = payment.createdAt,
        paidAt = payment.paidAt,
        updatedAt = payment.updatedAt,
        status = payment.status.name,
    )
}
