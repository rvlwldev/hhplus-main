package io.hhplus.concert.domain.seat

interface SeatRepository {
    fun save(seat: Seat): Seat

    fun findById(id: Long): Seat?
    fun findByScheduleIdAndNumber(scheduleId: Long, number: Long): Seat?
    fun findAllByScheduleId(scheduleId: Long): List<Seat>
}