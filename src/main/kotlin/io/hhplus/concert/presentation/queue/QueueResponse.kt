package io.hhplus.concert.presentation.queue

import java.time.LocalDateTime

data class QueueResponse(
    val token: String,
    val createdAt: LocalDateTime,
    val type: String,
)