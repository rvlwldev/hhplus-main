package io.hhplus.concert.domain.concert.entity

import io.hhplus.concert.domain.user.entity.User
import jakarta.persistence.*

enum class SeatStatus {
    EMPTY, WAIT, PAID
}

@Entity
@Table(name = "concert_seat")
class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "number")
    val seatNumber: Int = 0,

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    val schedule: Schedule = Schedule()
) {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    var user: User? = null
        protected set

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    var status: SeatStatus = SeatStatus.EMPTY
        protected set

    fun reserve(user: User) {
        this.user = user
        this.status = SeatStatus.WAIT
    }

    fun confirm() {
        if (this.user == null) throw IllegalStateException("유저 정보가 누락되었습니다.")
        this.status = SeatStatus.PAID
    }
}
