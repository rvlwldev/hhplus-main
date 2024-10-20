package io.hhplus.concert.domain.queue

data class QueueResponse(
    val userId: Long,
    val rank: Long,
    val scheduleId: Long,
    val status: String
) {
    constructor(queue: Queue) : this(
        userId = queue.user.id,
        rank = queue.schedule.getUserRank(queue.user.id),
        scheduleId = queue.schedule.id,
        status = queue.status.name
    )
}