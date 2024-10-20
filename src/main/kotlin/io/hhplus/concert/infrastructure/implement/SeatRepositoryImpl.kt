package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.infrastructure.jpa.SeatJpaRepository
import org.springframework.stereotype.Repository

@Repository
class SeatRepositoryImpl(private val jpa: SeatJpaRepository) : SeatRepository {
    override fun save(seat: Seat): Seat =
        jpa.save(seat)

    override fun save(schedule: Schedule, number: Int): Seat =
        jpa.save(Seat(schedule = schedule, seatNumber = number))

    override fun findByScheduleIdAndNumber(scheduleId: Long, number: Int): Seat? =
        jpa.findByScheduleIdAndNumber(scheduleId, number).orElse(null)

    override fun findAllByScheduleId(scheduleId: Long): List<Seat> =
        jpa.findAllByScheduleId(scheduleId)
}