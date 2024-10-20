package io.hhplus.concert.domain.concert

import io.hhplus.concert.domain.schedule.ScheduleResponse

data class ConcertResponse(
    val id: Long,
    val name: String,
    val maximumAudienceCount: Long,
    val schedules: List<ScheduleResponse>
) {
    constructor(concert: Concert) : this(
        id = concert.id,
        name = concert.name,
        maximumAudienceCount = concert.maximumAudienceCount,
        schedules = concert.schedules
            .map { ScheduleResponse(it) }
    )
}