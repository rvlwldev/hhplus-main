package io.hhplus.concert.domain.payment

import io.hhplus.concert.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Payment(

    @Id
    val id: Long = 0L,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @Column(name = "amount")
    val amount: Long = 0L,

    @Column(name = "paid_at")
    val paidAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(user: User, amount: Long) : this(
        user = user,
        amount = amount,
        paidAt = LocalDateTime.now()
    )
}