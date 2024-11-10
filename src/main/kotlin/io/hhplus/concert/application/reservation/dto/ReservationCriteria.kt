package io.hhplus.concert.application.reservation.dto

data class ReservationCriteria(
    val userId: Long,
    val concertId: Long,
    val scheduleId: Long,
)