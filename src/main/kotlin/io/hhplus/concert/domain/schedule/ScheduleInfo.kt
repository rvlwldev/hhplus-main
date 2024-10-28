package io.hhplus.concert.domain.schedule

import java.time.LocalDateTime

data class ScheduleInfo(
    val scheduleId: Long,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    constructor(schedule: Schedule) : this(
        scheduleId = schedule.id,
        sttAt = schedule.sttAt,
        endAt = schedule.endAt,
    )
}