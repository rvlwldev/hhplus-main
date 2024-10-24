package io.hhplus.concert.domain.schedule

import io.hhplus.concert.domain.seat.SeatStatus
import java.time.LocalDateTime

data class ScheduleInfo(
    val scheduleId: Long,
    val concertId: Long,
    val concertName: String,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
    val reservableSeatCount: Long,
) {
    constructor(schedule: Schedule) : this(
        scheduleId = schedule.id,
        concertId = schedule.concert.id,
        concertName = schedule.concert.name,
        sttAt = schedule.sttAt,
        endAt = schedule.endAt,
        reservableSeatCount = schedule.seats
            .filter { it.status == SeatStatus.EMPTY }
            .size.toLong()
    )
}