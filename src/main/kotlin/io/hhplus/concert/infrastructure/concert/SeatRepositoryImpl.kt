package io.hhplus.concert.infrastructure.concert

import io.hhplus.concert.domain.concert.entity.Seat
import io.hhplus.concert.domain.concert.repository.SeatRepository
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

interface SeatJpaRepository : JpaRepository<Seat, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.concertSchedule.scheduleId = :scheduleId AND s.seatId = :seatId")
    fun findSeatWithLock(scheduleId: Long, seatId: Long): Optional<Seat>
}

@Repository
class SeatRepositoryImpl(private val jpa: SeatJpaRepository) : SeatRepository {
    override fun findWithLock(scheduleId: Long, seatId: Long): Seat? =
        jpa.findSeatWithLock(scheduleId, seatId).orElse(null)

    override fun save(seat: Seat) = jpa.save(seat)
}