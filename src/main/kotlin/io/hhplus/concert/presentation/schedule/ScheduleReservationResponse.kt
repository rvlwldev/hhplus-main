package io.hhplus.concert.presentation.schedule

import io.hhplus.concert.application.reservation.ReservationResult
import java.time.LocalDateTime

data class ScheduleReservationResponse(
    val token: String,
    val type: String,
    val createdAt: LocalDateTime
) {
    constructor(result: ReservationResult) : this(
        token = result.token,
        type = result.type,
        createdAt = LocalDateTime.now()
    )
}