package io.hhplus.concert.domain.core.outbox

import io.hhplus.concert.core.exception.BizException
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class OutBoxService(private val repo: OutBoxRepository) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun save(outBox: OutBox) = OutBoxInfo(repo.save(outBox))

    fun isDone(entityId: Long, entityName: String) =
        repo.isExist(entityId, entityName)

    fun isDone(entity: Any): Boolean {
        val id = entity::class.java.fields
            .filter { it.name == "id" }
            .also { if (it.isEmpty()) throw BizException(HttpStatus.NOT_FOUND, "올바르지 않은 이벤트 기록 조회") }
            .let { it[0].toString().toLong() }
        val name = entity::class.java.simpleName
        return isDone(id, name)
    }

}