package io.hhplus.concert.infrastructure.task

import io.hhplus.concert.infrastructure.jpa.PaymentJpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentScheduler(private val repo: PaymentJpaRepository) {

    @Transactional
    @Scheduled(fixedRate = 1000 * 15)
    fun expirePayment() {
        val payments = repo.findAll()
            .filter { it.status.name == "WAIT" }
            .filter { !it.isPayable() }

        repo.saveAll(payments)
    }

    @Transactional
    @Scheduled(fixedRate = 1000 * 60 * 10)
    fun clearExpiredPayment() {
        val payments = repo.findAllByStatus("TIME_OUT")
        repo.deleteAll(payments)
    }

}