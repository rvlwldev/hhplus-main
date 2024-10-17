package io.hhplus.concert.infrastructure.payment

import io.hhplus.concert.domain.payment.PaymentRepository
import io.hhplus.concert.domain.payment.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

interface PaymentJpaRepository : JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId")
    fun findAllByUserId(userId: Long): List<Payment>

}

@Repository
class PaymentRepositoryImpl(
    private val jpa: PaymentJpaRepository
) : PaymentRepository {

    override fun findById(paymentId: Long): Payment? = jpa.findById(paymentId).orElse(null)

    override fun findAllByUserId(userId: Long): List<Payment> = jpa.findAllByUserId(userId)

    override fun save(payment: Payment) = jpa.save(payment)

}