package io.hhplus.concert.domain.seat

import io.hhplus.concert.domain.schedule.Schedule

interface SeatRepository {
    fun save(seat: Seat): Seat
    fun save(schedule: Schedule, number: Long): Seat

    fun findByScheduleIdAndNumber(scheduleId: Long, number: Long): Seat?

    fun findAllByScheduleId(scheduleId: Long): List<Seat>
}