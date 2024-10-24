package io.hhplus.concert.application.reservation

data class ReservationCriteria(
    val userId: Long,
    val concertId: Long,
    val scheduleId: Long,
)