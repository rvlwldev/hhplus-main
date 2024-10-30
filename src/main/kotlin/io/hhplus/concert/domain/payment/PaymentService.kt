package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class PaymentService(private val repo: PaymentRepository) {

    fun create(userId: Long, amount: Long): PaymentInfo {
        val payment = repo.findLatestByUserId(userId)

        if (payment != null && payment.status == PaymentStatus.WAIT)
            throw BizException(BizError.Payment.DUPLICATED)

        return repo.save(Payment(userId, amount))
            .run { PaymentInfo(this) }
    }

    fun getLatestByUserId(userId: Long) = repo.findLatestByUserId(userId)
        ?.run { PaymentInfo(this) }
        ?: throw BizException(BizError.Payment.NOT_FOUND)

    fun getAllUnpaid() = repo.findAllByStatus(PaymentStatus.WAIT)
        .map { PaymentInfo(it) }

    fun pay(userId: Long): PaymentInfo {
        val payment = repo.findLatestByUserId(userId)
            ?: throw BizException(BizError.Payment.NOT_FOUND)

        payment.pay()

        return payment
            .run { PaymentInfo(this) }
    }

}