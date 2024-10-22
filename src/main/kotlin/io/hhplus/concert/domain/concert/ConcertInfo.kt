package io.hhplus.concert.domain.concert

import io.hhplus.concert.domain.schedule.ScheduleInfo

data class ConcertInfo(
    val id: Long,
    val name: String,
    val maximumAudienceCount: Long,
    val schedules: List<ScheduleInfo>
) {
    constructor(concert: Concert) : this(
        id = concert.id,
        name = concert.name,
        maximumAudienceCount = concert.maximumAudienceCount,
        schedules = concert.schedules
            .map { ScheduleInfo(it) }
    )
}