package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Payment(

    @Transient
    val PAY_TIME_OUT_SECONDS: Long = 60 * 5,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @Column(name = "amount")
    val amount: Long = 0L,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    paidAt: LocalDateTime? = null,

    updatedAt: LocalDateTime? = null,

    status: PaymentStatus = PaymentStatus.WAIT
) {
    @Column(name = "updated_at", nullable = true)
    var updatedAt: LocalDateTime? = null
        protected set

    @Column(name = "paid_at", nullable = true)
    var paidAt: LocalDateTime? = null
        protected set

    @Column(name = "status", nullable = false)
    var status: PaymentStatus = PaymentStatus.WAIT
        protected set

    constructor(user: User, amount: Long) : this(
        user = user,
        amount = amount,
        status = PaymentStatus.WAIT,
        paidAt = null,
        updatedAt = null
    )

    fun pay() {
        if (status == PaymentStatus.SUCCESS)
            throw IllegalStateException("이미 처리된 결제건 입니다.")

        validateTimeOut()

        paidAt = LocalDateTime.now()
        status = PaymentStatus.SUCCESS
    }

    fun cancel() {
        if (status != PaymentStatus.SUCCESS)
            throw IllegalStateException("처리되지 않은 결제건 입니다.")

        updatedAt = LocalDateTime.now()
        status = PaymentStatus.CANCEL
    }

    fun validateTimeOut() {
        val now = LocalDateTime.now()
        val limit = createdAt.plusSeconds(PAY_TIME_OUT_SECONDS)

        if (now.isAfter(limit))
            throw IllegalStateException("결제 대기 시간이 초과되었습니다.\n다시 시도해주세요")
    }

}