package io.hhplus.concert.domain.payment.dto

import io.hhplus.concert.domain.payment.entity.Payment
import java.time.ZonedDateTime

data class PaymentResponse(
    val paymentId: Long,
    val createdAt: ZonedDateTime,
    val type: String,
    val point: Long,
) {
    constructor(payment: Payment) : this(
        paymentId = payment.paymentId,
        createdAt = payment.createdAt,
        type = payment.status.name,
        point = payment.point,
    )
}