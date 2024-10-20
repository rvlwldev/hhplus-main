package io.hhplus.concert.domain.concert.dto

import io.hhplus.concert.domain.concert.entity.Schedule
import io.hhplus.concert.domain.concert.entity.SeatStatus
import java.time.LocalDateTime

data class ScheduleResponse(
    val concertId: Long,
    val concertName: String,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
    val reservableSeatCount: Long,
) {
    constructor(schedule: Schedule) : this(
        concertId = schedule.concert.id,
        concertName = schedule.concert.name,
        sttAt = schedule.sttAt,
        endAt = schedule.endAt,
        reservableSeatCount = schedule.seats
            .filter { it.status == SeatStatus.EMPTY }
            .size.toLong()
    )
}