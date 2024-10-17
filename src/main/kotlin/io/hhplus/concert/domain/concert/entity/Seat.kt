package io.hhplus.concert.domain.concert.entity

import jakarta.persistence.*
import java.io.Serializable

data class SeatId(
    val seatId: Long = 0L,
    val concertSchedule: Long = 0L
) : Serializable

@Entity
@IdClass(SeatId::class)  // 복합키 클래스 설정
class Seat(
    @Id
    val seatId: Long = 0L,

    @Id
    @ManyToOne
    @JoinColumn(name = "concertScheduleId")
    val concertSchedule: ConcertSchedule = ConcertSchedule(),

    val userId: Long? = null
) {
    fun toLong() = seatId

    fun isAvailable() = userId == null
}