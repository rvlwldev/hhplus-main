package io.hhplus.concert.domain.queue

import java.time.ZonedDateTime


interface QueueRepository {
    fun findById(queueId: Long): Queue?
    fun findByUserIdAndScheduleId(userId: Long, scheduleId: Long): Queue?
    fun findWaitQueueWithSize(size: Int): List<Queue>
    fun findPassQueueByUserIdAndScheduleId(userId: Long, scheduleId: Long): Queue?
    fun save(queue: Queue): Queue
    fun deleteTimeout()
    fun deleteByUserIdAndScheduleId(userId: Long, scheduleId: Long)
    fun deleteByStatusAndUpdatedAtBefore(status: QueueStatus, updatedAtBefore: ZonedDateTime)

}