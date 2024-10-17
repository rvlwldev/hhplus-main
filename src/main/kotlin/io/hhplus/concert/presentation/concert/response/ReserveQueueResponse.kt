package io.hhplus.concert.presentation.concert.response

class ReserveQueueResponse(
    val concertId: Long,
    val name: String,
    val price: Long,
    val maximumAudienceCount: Long,
    val waitingQueueLength: Long
)