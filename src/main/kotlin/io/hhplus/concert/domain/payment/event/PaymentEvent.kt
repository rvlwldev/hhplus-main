package io.hhplus.concert.domain.payment.event

import io.hhplus.concert.domain.payment.PaymentInfo

class PaymentEvent {
    data class Init(
        val id: Long,
        val amount: Long
    ) {
        constructor(payment: PaymentInfo) : this(
            id = payment.id,
            amount = payment.amount,
        )
    }
}