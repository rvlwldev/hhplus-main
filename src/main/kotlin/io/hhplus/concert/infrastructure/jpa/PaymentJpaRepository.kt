package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<Payment, Long> {
}