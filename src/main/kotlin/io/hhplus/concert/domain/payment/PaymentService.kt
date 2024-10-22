package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val repo: PaymentRepository,
    private val userRepo: UserRepository
) {
    fun ready(userId: Long, amount: Long) {}

    fun pay(paymentId: Long) {}

    fun getHistoryList(userId: Long) {}
}