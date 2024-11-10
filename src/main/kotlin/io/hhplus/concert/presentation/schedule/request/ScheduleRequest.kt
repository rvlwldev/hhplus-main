package io.hhplus.concert.presentation.schedule.request

import java.time.LocalDateTime

data class ScheduleRequest(
    val maximumReservableCount: Long,
    val sttAt: LocalDateTime,
    val endAt: LocalDateTime,
    val sttReserveAt: LocalDateTime,
    val endReserveAt: LocalDateTime
)