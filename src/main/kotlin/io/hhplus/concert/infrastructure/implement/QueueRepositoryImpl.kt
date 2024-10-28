package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.infrastructure.jpa.QueueJpaRepository
import org.springframework.stereotype.Repository

@Repository
class QueueRepositoryImpl(private val jpa: QueueJpaRepository) : QueueRepository {
    override fun save(queue: Queue): Queue =
        jpa.save(queue)

    override fun findById(id: Long): Queue? =
        jpa.findById(id).orElse(null)

    override fun findByUserId(userId: Long): Queue? =
        jpa.findByUserId(userId).orElse(null)

    override fun findAllByScheduleId(scheduleId: Long): List<Queue> =
        jpa.findAllByScheduleId(scheduleId)

    override fun delete(queue: Queue) =
        jpa.delete(queue)

}