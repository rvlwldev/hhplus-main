package io.hhplus.concert.domain.schedule

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Schedule(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val concertId: Long,
    val sttAt: LocalDateTime = LocalDateTime.now(),
    val endAt: LocalDateTime = LocalDateTime.now(),
    val maximumReservableCount: Long = 0L,

    reservableCount: Long
) {
    var reservableCount = 50
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