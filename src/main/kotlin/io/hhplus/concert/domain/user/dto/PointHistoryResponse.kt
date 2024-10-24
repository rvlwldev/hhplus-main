package io.hhplus.concert.domain.user.dto

import io.hhplus.concert.domain.user.entity.UserPointHistory

data class PointHistoryResponse(
    val historyId: Long,
    val type: String,
    val point: Long
) {
    constructor(history: UserPointHistory) : this(
        historyId = history.historyId,
        type = history.type.toString(),
        point = history.user.point
    )
}