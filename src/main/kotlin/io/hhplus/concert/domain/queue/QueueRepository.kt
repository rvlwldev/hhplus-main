package io.hhplus.concert.domain.queue

interface QueueRepository {
    fun save(queue: Queue): Queue

    fun findById(id: Long): Queue?
    fun findByUserId(userId: Long): Queue?
    fun findAllByScheduleId(scheduleId: Long): List<Queue>

    fun delete(queue: Queue): Unit
}