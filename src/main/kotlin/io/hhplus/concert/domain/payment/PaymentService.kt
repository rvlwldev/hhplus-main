package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class PaymentService(private val repo: PaymentRepository) {

    fun create(userId: Long): PaymentInfo {
        val payment = repo.findByUserId(userId)
        if (payment != null && payment.status == PaymentStatus.WAIT)
            throw BizException(BizError.Payment.DUPLICATED)
        return repo.save(Payment(userId))
            .run { PaymentInfo(this) }
    }

    fun getByUserId(userId: Long) = repo.findByUserId(userId)
        ?.run { PaymentInfo(this) }
        ?: throw BizException(BizError.Payment.NOT_FOUND)

    fun pay(userId: Long): PaymentInfo {
        val payment = repo.findByUserId(userId)
            ?: throw BizException(BizError.Payment.NOT_FOUND)
        payment.pay()
        return payment
            .run { PaymentInfo(this) }
    }

}