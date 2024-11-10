package io.hhplus.concert.application.reservation.dto

import io.hhplus.concert.application.support.TokenManager

data class ReservationResult(
    val token: String,
    val type: String,
    val scheduleId: Long,
) {
    constructor(token: String, type: TokenManager.Type, scheduleId: Long) : this(
        token = token,
        type = type.name,
        scheduleId = scheduleId,
    )
}