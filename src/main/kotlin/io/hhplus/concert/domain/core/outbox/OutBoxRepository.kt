package io.hhplus.concert.domain.core.outbox

interface OutBoxRepository {
    fun save(outbox: OutBox): OutBox
    fun findAllNotDoneOrFail(): List<OutBox>
    fun findAllFail(): List<OutBox>
    fun isExist(entityId: Long, entityName: String): Boolean
    fun delete(outbox: OutBox)
    fun delete(outboxes: List<OutBox>)
}