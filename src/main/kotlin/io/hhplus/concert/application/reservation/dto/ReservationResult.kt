package io.hhplus.concert.application.reservation.dto

data class ReservationResult(
    val token: String,
    val type: String,
    val scheduleId: Long,
    val rank: Long
)