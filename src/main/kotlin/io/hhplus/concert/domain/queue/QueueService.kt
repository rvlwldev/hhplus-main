package io.hhplus.concert.domain.queue

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class QueueService(private val repo: QueueRepository) {

    fun create(userId: Long, scheduleId: Long): Queue {
        if (repo.findByUserId(userId) != null)
            throw BizException(BizError.Queue.DUPLICATED)

        val queue = Queue(userId, scheduleId)
        return repo.save(queue)
    }

    fun get(id: Long) = repo.findById(id)
        ?: throw BizException(BizError.Queue.NOT_FOUND)

    fun pass(id: Long): Queue {
        val queue = repo.findById(id)
            ?: throw BizException(BizError.Queue.NOT_FOUND)

        queue.pass()

        return queue
    }

    fun delete(id: Long) {
        val queue = repo.findById(id)
        if (queue != null) repo.delete(queue)
    }

}