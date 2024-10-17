package io.hhplus.concert.domain.concert.dto

import io.hhplus.concert.domain.concert.entity.Concert

data class ConcertResponse(
    val concertId: Long,
    val name: String,
    val price: Long
) {
    constructor(concert: Concert) : this(
        concertId = concert.concertId,
        name = concert.name,
        price = concert.price
    )
}