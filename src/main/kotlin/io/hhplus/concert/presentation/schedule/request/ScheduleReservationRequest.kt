package io.hhplus.concert.presentation.schedule.request

data class ScheduleReservationRequest(
    val userId: Long,
    val scheduleId: Long,
)