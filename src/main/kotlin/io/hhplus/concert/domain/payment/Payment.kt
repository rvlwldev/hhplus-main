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
    WAIT, PAID
}

@Entity
class Payment(

    @Transient
    val PAY_TIME_OUT_SECONDS: Long = 60 * 5,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val userId: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    updatedAt: LocalDateTime? = null,
    status: PaymentStatus = PaymentStatus.WAIT
) {
    var updatedAt: LocalDateTime? = null
        protected set

    var status = PaymentStatus.WAIT
        protected set

    fun pay() {
        val now = LocalDateTime.now()

        if (createdAt.plusSeconds(PAY_TIME_OUT_SECONDS) < now)
            throw BizException(BizError.Queue.TIME_OUT)

        updatedAt = now
        status = PaymentStatus.PAID
    }
}