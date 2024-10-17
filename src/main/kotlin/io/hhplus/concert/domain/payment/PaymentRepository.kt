package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.payment.entity.Payment

interface PaymentRepository {

    fun findById(paymentId: Long): Payment?
    fun findAllByUserId(userId: Long): List<Payment>
    fun save(payment: Payment): Payment

}