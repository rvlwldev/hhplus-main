package io.hhplus.concert.domain.user.dto

import io.hhplus.concert.domain.user.entity.UserPointHistory
import java.time.LocalDateTime

data class UserPointHistoryResponse(
    val userId: Long,
    val amount: Long,
    val type: String,
    val createdAt: LocalDateTime
) {
    constructor(history: UserPointHistory) : this(
        userId = history.user.id,
        amount = history.amount,
        type = history.type.name,
        createdAt = history.createdAt
    )
}