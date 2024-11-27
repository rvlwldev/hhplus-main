package io.hhplus.concert.infrastructure.kafka.outbox

import io.hhplus.concert.domain.core.outbox.OutBox
import io.hhplus.concert.domain.core.outbox.OutBoxRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
class OutBoxRepositoryImpl(private val jpa: OutBoxJpaRepository) : OutBoxRepository {

    override fun save(outbox: OutBox) =
        jpa.save(outbox)

    override fun findAllNotDoneOrFail() =
        jpa.findAllByStatus("PROGRESS")

    override fun findAllFail() =
        jpa.findAllByStatus("FAIL")

    override fun isExist(entityId: Long, entityName: String) =
        jpa.findByEntityIdAndEntityName(entityId, entityName) != null

    override fun delete(outbox: OutBox) =
        jpa.delete(outbox)

    override fun delete(outboxes: List<OutBox>) =
        jpa.deleteAll(outboxes)

}

interface OutBoxJpaRepository : JpaRepository<OutBox, Long> {

    @Query(
        "SELECT o " +
                " FROM OutBox o " +
                "WHERE o.topic      = :topic" +
                "  AND o.entityId   = :id" +
                "  AND o.entityName = :name"
    )
    fun findEvent(topic: String, id: Long, name: String): OutBox?

    fun findAllByStatus(status: String): List<OutBox>

    fun findByEntityIdAndEntityName(id: Long, name: String): OutBox?

}