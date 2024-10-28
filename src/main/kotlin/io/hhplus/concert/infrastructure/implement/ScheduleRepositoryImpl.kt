package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleRepository
import io.hhplus.concert.infrastructure.jpa.ScheduleJpaRepository
import org.springframework.stereotype.Repository

@Repository
class ScheduleRepositoryImpl(private val jpa: ScheduleJpaRepository) : ScheduleRepository {
    override fun save(schedule: Schedule): Schedule =
        jpa.save(schedule)

    override fun findById(id: Long): Schedule? =
        jpa.findById(id).orElse(null)

    override fun findAllByConcertId(concertId: Long): List<Schedule> =
        jpa.findAllByConcertId(concertId)
}