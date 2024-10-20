package io.hhplus.concert.domain.pointHistory

import java.time.LocalDateTime

data class PointHistoryResponse(
    val userId: Long,
    val amount: Long,
    val type: String,
    val createdAt: LocalDateTime
) {
    constructor(history: PointHistory) : this(
        userId = history.user.id,
        amount = history.amount,
        type = history.type.name,
        createdAt = history.createdAt
    )
}