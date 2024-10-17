package io.hhplus.concert.domain.queue


interface QueueRepository {
    fun findById(queueId: Long): Queue?
    fun findByUserIdAndScheduleId(userId: Long, scheduleId: Long): Queue?
    fun findWaitQueueWithSize(size: Int): List<Queue>
    fun save(queue: Queue): Queue
    fun deleteTimeout()
}