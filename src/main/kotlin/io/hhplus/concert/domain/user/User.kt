package io.hhplus.concert.domain.user

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "name")
    val name: String = "",

    point: Long = 0L

) {
    @Column(name = "point")
    var point: Long = 0L
        protected set

    fun usePoint(amount: Long) {
        if (amount <= 0)
            throw BizException(BizError.Payment.INVALID_AMOUNT)

        if (point < amount)
            throw BizException(BizError.Payment.NOT_ENOUGH)

        point -= amount
    }

    fun chargePoint(amount: Long) {
        if (amount <= 0)
            throw BizException(BizError.Payment.INVALID_AMOUNT)

        point += amount
    }
}
