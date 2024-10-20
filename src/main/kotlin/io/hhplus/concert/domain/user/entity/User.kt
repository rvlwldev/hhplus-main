package io.hhplus.concert.domain.user.entity

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
        if (amount <= 0) throw IllegalArgumentException("1보다 작은 포인트는 사용 불가")
        if (point < amount) throw IllegalArgumentException("보유 포인트 부족")
        point -= amount
    }

    fun chargePoint(amount: Long) {
        if (amount <= 0) throw IllegalArgumentException("1보다 작은 포인트는 충전 불가")
        point += amount
    }
}
