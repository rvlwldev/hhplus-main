package io.hhplus.concert.domain.queue

data class QueueInfo(
    val id: Long,
    val userId: Long,
    val scheduleId: Long,
    val status: String
) {
    constructor(queue: Queue) : this(
        id = queue.id,
        userId = queue.userId,
        scheduleId = queue.scheduleId,
        status = queue.status.name
    )
}