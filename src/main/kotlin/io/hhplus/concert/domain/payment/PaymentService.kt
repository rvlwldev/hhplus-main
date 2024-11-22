package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.payment.event.PaymentEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val repo: PaymentRepository,
    private val publisher: ApplicationEventPublisher
) {

    fun create(userId: Long, amount: Long): PaymentInfo {
        val payment = repo.findLatestByUserId(userId)

        if (payment != null && payment.status == PaymentStatus.WAIT) throw BizException(BizError.Payment.DUPLICATED)

        return repo.save(Payment(userId, amount)).run { PaymentInfo(this) }
    }

    fun getLatestByUserId(userId: Long) =
        repo.findLatestByUserId(userId)?.run { PaymentInfo(this) } ?: throw BizException(BizError.Payment.NOT_FOUND)

    fun getAllUnpaid() = repo.findAllByStatus(PaymentStatus.WAIT)
        .map { PaymentInfo(it) }

    @Transactional
    fun pay(userId: Long): PaymentInfo {
        val payment = repo.findLatestByUserId(userId)
            ?: throw BizException(BizError.Payment.NOT_FOUND)

        payment.pay()

        val result = PaymentInfo(repo.save(payment))
        val event = PaymentEvent.Init(result)

        publisher.publishEvent(event)
        return result
    }

}