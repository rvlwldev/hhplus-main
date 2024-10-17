package io.hhplus.concert.presentation.user.response

data class PointHistoryResponse(
    val historyId: Long,
    val type: String,
    val point: Long
)