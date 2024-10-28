package io.hhplus.concert.domain.concert

data class ConcertInfo(
    val id: Long,
    val name: String,
    val price: Long,
    val maximumAudienceCount: Long
) {
    constructor(concert: Concert) : this(
        id = concert.id,
        name = concert.name,
        price = concert.price,
        maximumAudienceCount = concert.maximumAudienceCount
    )
}