package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.infrastructure.jpa.SeatJpaRepository
import org.springframework.stereotype.Repository

@Repository
class SeatRepositoryImpl(private val jpa: SeatJpaRepository) : SeatRepository {
    override fun save(seat: Seat): Seat =
        jpa.save(seat)

    override fun findById(id: Long): Seat? =
        jpa.findById(id).orElse(null)

    override fun findByScheduleIdAndNumber(scheduleId: Long, number: Long): Seat? =
        jpa.findByScheduleIdAndNumber(scheduleId, number).orElse(null)

    override fun findAllByScheduleId(scheduleId: Long): List<Seat> =
        jpa.findAllByScheduleId(scheduleId)
}