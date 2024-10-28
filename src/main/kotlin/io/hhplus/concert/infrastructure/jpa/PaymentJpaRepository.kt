package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PaymentJpaRepository : JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId")
    fun findByUserId(userId: Long): Optional<Payment>

    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    fun findAllByStatus(status: String): List<Payment>
}