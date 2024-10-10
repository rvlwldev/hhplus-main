package io.hhplus.concert.presentation.concert.response

data class ConcertResponse(
    val concertId: Long,
    val name: String,
    val price: Long
)