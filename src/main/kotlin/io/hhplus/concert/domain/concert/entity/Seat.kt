package io.hhplus.concert.domain.concert.entity

import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import java.io.Serializable
import java.time.ZonedDateTime

enum class SeatStatus {
    EMPTY, WAIT, RESERVED
}

data class SeatId(
    val seatId: Long = 0L,
    val concertSchedule: Long = 0L
) : Serializable

@Entity
@IdClass(SeatId::class)
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["seatId", "scheduleId"])])
class Seat(
    @Id
    val seatId: Long = 0L,

    @Id
    @ManyToOne
    @JoinColumn(name = "scheduleId")
    val concertSchedule: ConcertSchedule = ConcertSchedule(),

    var userId: Long? = null,

    @Enumerated(EnumType.STRING)
    var status: SeatStatus = SeatStatus.EMPTY,

    val createdAt: ZonedDateTime = ZonedDateTime.now()
) {
    fun reserve() {
        if (status != SeatStatus.EMPTY) throw BizException(HttpStatus.BAD_REQUEST, "이미 선점된 좌석입니다.")
        status = SeatStatus.WAIT
    }

    fun confirm() {
        status = SeatStatus.RESERVED
    }

    fun cancel() {
        status = SeatStatus.EMPTY
    }
}