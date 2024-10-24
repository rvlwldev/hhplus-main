package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.queue.Queue
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface QueueJpaRepository : JpaRepository<Queue, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun save(queue: Queue): Queue

    @Query("SELECT q FROM QUEUE q WHERE q.user.id = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByUserId(userId: Long): Optional<Queue>

    @Query("SELECT q FROM QUEUE q WHERE q.schedule.id = :scheduleId")
    fun findAllByScheduleId(scheduleId: Long): List<Queue>
}