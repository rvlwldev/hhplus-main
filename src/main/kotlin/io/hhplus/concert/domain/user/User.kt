package io.hhplus.concert.domain.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
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
            throw IllegalArgumentException("1보다 작은 포인트는 사용할 수 없습니다.")

        if (point < amount)
            throw IllegalArgumentException("보유 포인트가 부족합니다.")

        point -= amount
    }

    fun chargePoint(amount: Long) {
        if (amount <= 0)
            throw IllegalArgumentException("1보다 작은 포인트는 충전할 수 없습니다.")

        point += amount
    }
}
