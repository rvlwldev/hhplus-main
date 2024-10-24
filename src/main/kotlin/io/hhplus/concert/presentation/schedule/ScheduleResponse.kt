package io.hhplus.concert.presentation.schedule

import io.hhplus.concert.domain.schedule.ScheduleInfo

data class ScheduleResponse(
    val scheduleId: Long,
    val concertName: String,
    val reservableSeatCount: Long
) {
    constructor(info: ScheduleInfo) : this(
        scheduleId = info.scheduleId,
        concertName = info.concertName,
        reservableSeatCount = info.reservableSeatCount
    )
}

