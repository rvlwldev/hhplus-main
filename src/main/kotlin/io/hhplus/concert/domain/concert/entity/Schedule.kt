package io.hhplus.concert.domain.concert.entity

import io.hhplus.concert.domain.queue.Queue
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "concert_schedule")
class Schedule(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    val concert: Concert = Concert(),

    @Column(name = "start_at")
    val sttAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_at")
    val endAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schedule")
    val queueList: List<Queue> = ArrayList(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: List<Seat> = ArrayList()

) {
    fun isReservable() =
        seats
            .map { it.status }
            .any { it == SeatStatus.EMPTY }

    fun isReservable(seatNumber: Long) =
        seats.find { it.id == seatNumber }?.let { it.status == SeatStatus.EMPTY }
            ?: throw IllegalArgumentException("존재 하지 않는 좌석입니다.")

    fun getUserRank(userId: Long): Long {
        val queue = queueList.find { it.user.id == userId }
            ?: throw IllegalArgumentException("해당 유저는 대기열에 없습니다.")

        return queueList
            .count { it.createdAt < queue.createdAt }
            .toLong()
    }

}