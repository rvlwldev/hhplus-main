package io.hhplus.concert.domain.seat

import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.user.User
import jakarta.persistence.*


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

    fun confirm(user: User) {
        if (this.user != user) throw IllegalStateException("올바르지 않은 요청입니다.")
        if (this.status != SeatStatus.WAIT) throw IllegalStateException("선점할 수 없는 좌석입니다.")
        this.status = SeatStatus.PAID
    }
}
