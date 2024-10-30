package io.hhplus.concert.presentation.schedule.response

import io.hhplus.concert.domain.schedule.ScheduleInfo
import java.time.LocalDateTime

data class ScheduleResponse(
    val scheduleId: Long,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    constructor(info: ScheduleInfo) : this(
        scheduleId = info.id,
        sttAt = info.sttAt,
        endAt = info.endAt,
    )
}

