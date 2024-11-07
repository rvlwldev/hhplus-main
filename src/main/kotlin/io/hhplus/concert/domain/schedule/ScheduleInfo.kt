package io.hhplus.concert.domain.schedule

import java.time.LocalDateTime

data class ScheduleInfo(
    val id: Long,
    val sttAt: LocalDateTime,
    val sttReserveAt: LocalDateTime,
    val endAt: LocalDateTime,
    val endReserveAt: LocalDateTime
) {
    constructor(schedule: Schedule) : this(
        id = schedule.id,
        sttAt = schedule.sttAt,
        endAt = schedule.endAt,
        sttReserveAt = schedule.sttReserveAt,
        endReserveAt = schedule.endReserveAt
    )
}