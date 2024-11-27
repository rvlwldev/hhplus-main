package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Transient
import java.time.LocalDateTime

enum class PaymentStatus {
    WAIT, PAID, TIME_OUT, CANCEL
}

@Entity
class Payment(

    @Transient
    val PAY_TIME_OUT_SECONDS: Long = 60 * 5,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val userId: Long = 0L,
    val amount: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    updatedAt: LocalDateTime? = null,
    status: PaymentStatus = PaymentStatus.WAIT
) {
    var updatedAt: LocalDateTime? = updatedAt
        protected set

    var status = status
        protected set

    fun pay() {
        val now = LocalDateTime.now()

        if (createdAt.plusSeconds(PAY_TIME_OUT_SECONDS) < now)
            throw BizException(BizError.Payment.TIME_OUT)

        if (status != PaymentStatus.WAIT)
            throw BizException(BizError.Payment.FAILED)

        updatedAt = now
        status = PaymentStatus.PAID
    }

    fun isPayable(): Boolean {
        val now = LocalDateTime.now();

        val result = status == PaymentStatus.WAIT
                && createdAt.plusSeconds(PAY_TIME_OUT_SECONDS).isBefore(now)

        if (!result) status = PaymentStatus.TIME_OUT

        return result
    }

    fun cancel() {
        status = PaymentStatus.CANCEL
    }
}