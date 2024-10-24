package io.hhplus.concert.presentation.concert

import io.hhplus.concert.domain.concert.ConcertInfo

data class ConcertResponse(
    val concertId: Long,
    val name: String,
    val price: Long,
) {
    constructor(concert: ConcertInfo) : this(
        concertId = concert.id,
        name = concert.name,
        price = concert.price
    )
}