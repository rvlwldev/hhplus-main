package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.seat.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface SeatJpaRepository : JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.scheduleId = :scheduleId AND s.seatNumber = :seatNumber")
    fun findByScheduleIdAndNumber(scheduleId: Long, seatNumber: Long): Optional<Seat>

    @Query("SELECT s FROM Seat s WHERE s.scheduleId = :scheduleId")
    fun findAllByScheduleId(scheduleId: Long): List<Seat>
}
