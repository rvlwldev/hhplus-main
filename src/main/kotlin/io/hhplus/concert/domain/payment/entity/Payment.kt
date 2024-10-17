package io.hhplus.concert.domain.payment.entity

import io.hhplus.concert.domain.payment.dto.PaymentResponse
import io.hhplus.concert.domain.user.entity.User
import jakarta.persistence.*
import java.time.ZoneId
import java.time.ZonedDateTime

enum class PayStatus {
    READY, SUCCESS, CANCEL
}

@Entity
class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paymentId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,

    val status: PayStatus,

    val point: Long,

    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

) {

    fun toDTO() = PaymentResponse(this)
}