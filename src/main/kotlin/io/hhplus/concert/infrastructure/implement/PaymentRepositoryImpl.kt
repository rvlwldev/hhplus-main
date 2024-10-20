package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.payment.Payment
import io.hhplus.concert.domain.payment.PaymentRepository
import io.hhplus.concert.domain.payment.PaymentStatus
import io.hhplus.concert.infrastructure.jpa.PaymentJpaRepository

class PaymentRepositoryImpl(private val jpa: PaymentJpaRepository) : PaymentRepository {
    override fun save(payment: Payment): Payment =
        jpa.save(payment)

    override fun findById(id: Long): Payment? =
        jpa.findById(id).orElse(null)

    override fun findByUserId(userId: Long): Payment? =
        jpa.findByUserId(userId).orElse(null)

    override fun findAllByStatus(status: PaymentStatus): List<Payment> =
        jpa.findAllByStatus(status.name)
}