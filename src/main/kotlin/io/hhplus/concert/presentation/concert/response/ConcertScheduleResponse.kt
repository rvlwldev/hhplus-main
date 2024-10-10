package io.hhplus.concert.presentation.concert.response

data class ConcertScheduleResponse(
    val scheduleId: Long,
    val startDatetime: String,
    val endDatetime: String,
    val maximumAudienceCount: Long,
    val reservableAudienceCount: Long
)