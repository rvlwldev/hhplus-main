package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.schedule.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ScheduleJpaRepository : JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.concert.id = :concertId")
    fun findAllByConcertId(concertId: Long): List<Schedule>
}