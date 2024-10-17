package io.hhplus.concert.presentation.payment.response

data class PaymentResponse(
    val paymentId: Long,
    val concertId: Long,
    val point: Long,
    val startDatetime: String,
    val endDatetime: String,
)