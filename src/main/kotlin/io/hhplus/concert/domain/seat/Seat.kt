package io.hhplus.concert.domain.seat

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

enum class SeatStatus {
    EMPTY, WAIT, PAID, FAIL
}

@Entity
class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val scheduleId: Long,
    val seatNumber: Long,

    userId: Long? = null,
    status: SeatStatus = SeatStatus.EMPTY
) {
    var userId: Long? = null
        protected set

    var status = SeatStatus.EMPTY
        protected set

    fun readyToReserve(userId: Long) {
        if (status !== SeatStatus.EMPTY)
            throw BizException(BizError.Seat.ALREADY_RESERVED)

        this.userId = userId
        status = SeatStatus.WAIT
    }

    fun confirmReservation() {
        if (userId == null)
            throw BizException(BizError.Seat.TOO_LATE_TO_RESERVE)
        status = SeatStatus.PAID
    }

    fun cancel() {
        userId = null
        status = SeatStatus.EMPTY
    }
}
