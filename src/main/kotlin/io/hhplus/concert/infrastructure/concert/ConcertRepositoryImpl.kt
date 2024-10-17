package io.hhplus.concert.infrastructure.concert

import io.hhplus.concert.domain.concert.ConcertRepository
import io.hhplus.concert.domain.concert.entity.Concert
import io.hhplus.concert.domain.concert.entity.ConcertSchedule
import io.hhplus.concert.domain.concert.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

interface ConcertJpaRepository : JpaRepository<Concert, Long>

interface ConcertScheduleJpaRepository : JpaRepository<ConcertSchedule, Long> {

    @Query("SELECT s FROM ConcertSchedule s WHERE s.concert.concertId = :concertId")
    fun findAllByConcertId(concertId: Long): List<ConcertSchedule>

    @Query("SELECT s.seats FROM ConcertSchedule s WHERE s.scheduleId = :scheduleId")
    fun findAllSeatById(scheduleId: Long): List<Seat>

}

@Repository
class ConcertRepositoryImpl(
    private val jpa: ConcertJpaRepository,
    private val scheduleJpa: ConcertScheduleJpaRepository
) : ConcertRepository {

    override fun findScheduleById(scheduleId: Long): ConcertSchedule? = scheduleJpa.findById(scheduleId)
        .orElse(null)

    override fun findById(concertId: Long): Concert? = jpa.findById(concertId)
        .orElse(null)

    override fun findAll(): List<Concert> = jpa.findAll()

    override fun findAllScheduleByConcertId(concertId: Long) = scheduleJpa.findAllByConcertId(concertId)

    override fun findAllSeatByScheduleId(scheduleId: Long) = scheduleJpa.findAllSeatById(scheduleId)

}