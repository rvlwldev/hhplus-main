package io.hhplus.concert.application.reservation.dto

data class ReservationResultV2(
    val token: String,
    val type: String,
    val scheduleId: Long,
    val rank: Long
)