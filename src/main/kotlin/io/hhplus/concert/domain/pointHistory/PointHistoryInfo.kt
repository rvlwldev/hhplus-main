package io.hhplus.concert.domain.pointHistory

import java.time.LocalDateTime

data class PointHistoryInfo(
    val userId: Long,
    val amount: Long,
    val type: String,
    val createdAt: LocalDateTime
) {
    constructor(history: PointHistory) : this(
        userId = history.userId,
        amount = history.amount,
        type = history.type.name,
        createdAt = history.createdAt
    )
}