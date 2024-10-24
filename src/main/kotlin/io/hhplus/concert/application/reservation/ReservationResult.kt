package io.hhplus.concert.application.reservation

import io.hhplus.concert.config.support.TokenManager
import io.hhplus.concert.domain.queue.QueueInfo

data class ReservationResult(
    val token: String,
    val type: String,
    val scheduleId: Long,
    val rank: Long
) {
    constructor(token: String, type: TokenManager.Type, queue: QueueInfo) : this(
        token = token,
        type = type.name,
        scheduleId = queue.scheduleId,
        rank = queue.rank
    )
}