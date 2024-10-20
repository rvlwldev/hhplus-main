package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.schedule.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleJpaRepository : JpaRepository<Schedule, Long> {
}