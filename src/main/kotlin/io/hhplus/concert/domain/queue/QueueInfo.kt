package io.hhplus.concert.domain.queue

data class QueueInfo(
    val userId: Long,
    val scheduleId: Long,
    val status: String
) {
    constructor(queue: Queue) : this(
        userId = queue.userId,
        scheduleId = queue.scheduleId,
        status = queue.status.name
    )
}