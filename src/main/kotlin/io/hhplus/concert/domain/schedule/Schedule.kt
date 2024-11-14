package io.hhplus.concert.domain.schedule

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "schedule",
    indexes = [
        Index(name = "idx_schedule_concert_id", columnList = "concert_id"),
        Index(name = "idx_schedule_stt_at", columnList = "stt_at"),
        Index(name = "idx_schedule_concert_id_stt_at", columnList = "concert_id, stt_at"),
        Index(name = "idx_schedule_reserve_period", columnList = "stt_reserve_at, end_reserve_at"),
        Index(name = "idx_schedule_concert_id_reserve_period", columnList = "concert_id, stt_reserve_at, end_reserve_at")
    ]
)
class Schedule(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "concert_id")
    val concertId: Long,

    @Column(name = "stt_at")
    val sttAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_at")
    val endAt: LocalDateTime = LocalDateTime.now().plusHours(2),

    @Column(name = "stt_reserve_at")
    val sttReserveAt: LocalDateTime = sttAt.minusWeeks(1),

    @Column(name = "end_reserve_at")
    val endReserveAt: LocalDateTime = sttAt.minusWeeks(1),

    val maximumReservableCount: Long = 0L,
    reservableCount: Long
) {
    constructor() : this(
        concertId = 0L,
        reservableCount = 0
    )

    var reservableCount = maximumReservableCount
        protected set

    fun decreaseReservableCount() {
        if (reservableCount < 1) throw BizException(BizError.Schedule.NOT_ENOUGH_AVAILABLE_SEAT)
        reservableCount--
    }

    fun increaseReservableCount() {
        if (reservableCount >= maximumReservableCount) throw BizException(BizError.Schedule.NOT_ALLOWED_CANCEL)
        reservableCount--
    }

}