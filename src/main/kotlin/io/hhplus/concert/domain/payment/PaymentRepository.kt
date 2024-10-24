package io.hhplus.concert.domain.payment

interface PaymentRepository {
    fun save(payment: Payment): Payment

    fun findById(id: Long): Payment?
    fun findByUserId(userId: Long): Payment?
    fun findAllByStatus(status: PaymentStatus): List<Payment>
  
}