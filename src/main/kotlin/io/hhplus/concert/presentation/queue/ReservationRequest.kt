package io.hhplus.concert.presentation.queue

data class ReservationRequest(
    val userId: Long,
    val scheduleId: Long,
)