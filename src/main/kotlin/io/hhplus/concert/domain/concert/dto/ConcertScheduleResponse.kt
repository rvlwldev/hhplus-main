package io.hhplus.concert.domain.concert.dto

import io.hhplus.concert.domain.concert.entity.ConcertSchedule

data class ConcertScheduleResponse(
    val scheduleId: Long,
    val startDatetime: String,
    val endDatetime: String,
    val maximumAudienceCount: Long
) {

    constructor(schedule: ConcertSchedule) : this(
        scheduleId = schedule.scheduleId,
        startDatetime = schedule.startDateTime.toString(),
        endDatetime = schedule.endDateTime.toString(),
        maximumAudienceCount = schedule.maximumAudienceCount,
    )

}