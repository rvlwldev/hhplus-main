package io.hhplus.concert.domain.core.outbox

data class OutBoxInfo(
    val entityId: Long,
    val entityName: String,
    val topic: String,
) {
    constructor(outBox: OutBox) : this(
        entityId = outBox.entityId,
        entityName = outBox.entityName,
        topic = outBox.topic,
    )
}

