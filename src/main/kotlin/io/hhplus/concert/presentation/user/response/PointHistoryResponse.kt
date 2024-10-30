package io.hhplus.concert.presentation.user.response

import io.hhplus.concert.domain.pointHistory.PointHistoryInfo
import java.time.LocalDateTime

class PointHistoryResponse(
    val amount: Long,
    val type: String,
    val createdAt: LocalDateTime
) {
    constructor(history: PointHistoryInfo) : this(
        amount = history.amount,
        type = history.type,
        createdAt = history.createdAt
    )
}