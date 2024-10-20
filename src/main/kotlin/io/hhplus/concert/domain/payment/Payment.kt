package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @Column(name = "amount")
    val amount: Long = 0L,

    paidAt: LocalDateTime? = null,

    updatedAt: LocalDateTime? = null,

    status: PaymentStatus = PaymentStatus.WAIT
) {
    @Column(name = "paid_at", nullable = true)
    var paidAt: LocalDateTime? = null
        protected set

    @Column(name = "updated_at", nullable = true)
    var updatedAt = null
        protected set

    @Column(name = "status")
    var status = PaymentStatus.WAIT
        protected set

    constructor(user: User, amount: Long) : this(
        user = user,
        amount = amount,
        status = PaymentStatus.WAIT,
        paidAt = null,
        updatedAt = null
    )
}