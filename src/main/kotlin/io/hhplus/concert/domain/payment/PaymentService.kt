package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class PaymentService(private val repo: PaymentRepository) {

    fun create(userId: Long): Payment {
        val payment = repo.findByUserId(userId)
        if (payment != null && payment.status == PaymentStatus.WAIT)
            throw BizException(BizError.Payment.DUPLICATED)
        return repo.save(Payment(userId))
    }

    fun getByUserId(userId: Long) = repo.findByUserId(userId)
        ?: throw BizException(BizError.Payment.NOT_FOUND)

    fun pay(userId: Long): Payment {
        val payment = repo.findByUserId(userId)
            ?: throw BizException(BizError.Payment.NOT_FOUND)
        payment.pay()
        return payment
    }

}