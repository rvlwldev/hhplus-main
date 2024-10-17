package io.hhplus.concert.domain.concert.repository

import io.hhplus.concert.domain.concert.entity.Seat

interface SeatRepository {

    fun findWithLock(scheduleId: Long, seatId: Long): Seat?
    fun save(seat: Seat): Seat
}