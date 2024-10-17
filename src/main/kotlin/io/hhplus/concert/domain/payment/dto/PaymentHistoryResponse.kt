package io.hhplus.concert.domain.payment.dto

data class PaymentHistoryResponse(
    val paymentId: Long,
    val concertId: Long,
    val scheduleId: Long,
    val type: String,
    val createdAt: String,
    val point: Long,
)