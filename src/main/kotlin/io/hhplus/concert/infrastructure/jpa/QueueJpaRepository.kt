package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.queue.Queue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface QueueJpaRepository : JpaRepository<Queue, Long> {
    @Query("SELECT q FROM QUEUE q WHERE q.user.id = :userId")
    fun findByUserId(userId: Long): Optional<Queue>

    @Query("SELECT q FROM QUEUE q WHERE q.schedule.id = :scheduleId")
    fun findAllByScheduleId(scheduleId: Long): List<Queue>
}