package io.hhplus.concert.domain.payment

import java.time.LocalDateTime

data class PaymentInfo(
    val paymentId: Long,
    val amount: Long,
    val createdAt: LocalDateTime,
    val status: String,
    val updatedAt: LocalDateTime?
) {
    constructor(payment: Payment) : this(
        paymentId = payment.id,
        amount = payment.amount,
        status = payment.status.name,
        createdAt = payment.createdAt,
        updatedAt = payment.updatedAt
    )
}
