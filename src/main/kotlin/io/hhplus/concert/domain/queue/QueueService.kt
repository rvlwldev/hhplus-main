package io.hhplus.concert.domain.queue

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class QueueService(private val repo: QueueRepository) {

    fun create(userId: Long, scheduleId: Long): QueueInfo {
        if (repo.findByUserId(userId) != null)
            throw BizException(BizError.Queue.DUPLICATED)

        return Queue(userId, scheduleId)
            .let { repo.save(it) }
            .run { QueueInfo(this) }
    }

    fun get(id: Long) = repo.findById(id)
        ?.run { QueueInfo(this) }
        ?: throw BizException(BizError.Queue.NOT_FOUND)

    fun pass(id: Long): QueueInfo {
        val queue = repo.findById(id)
            ?: throw BizException(BizError.Queue.NOT_FOUND)

        queue.pass()

        return repo.save(queue)
            .run { QueueInfo(this) }
    }

    fun delete(id: Long) {
        val queue = repo.findById(id)
        if (queue != null) repo.delete(queue)
    }

}