package io.hhplus.concert.domain.queue

import io.hhplus.concert.core.exception.BizException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueueService(private val repo: QueueRepository) {

    @Transactional
    fun create(userId: Long, scheduleId: Long): QueueInfo {
        val queue = repo.findByUserIdAndScheduleId(userId, scheduleId)
        if (queue != null) throw BizException(HttpStatus.CONFLICT, "이미 대기열에 등록된 유저입니다.")

        return repo.save(Queue(userId, scheduleId)).toDTO()
    }

    @Transactional
    fun get(userId: Long, scheduleId: Long): QueueInfo =
        repo.findByUserIdAndScheduleId(userId, scheduleId)?.toDTO()
            ?: throw BizException(HttpStatus.NOT_FOUND, "등록되지 않은 대기열 입니다.")

    @Transactional
    fun getAllWaitQueueBySize(size: Int): List<QueueInfo> =
        repo.findWaitQueueWithSize(size)
            .map { it.toDTO() }

    @Transactional
    fun pass(queueId: Long) {
        val queue = repo.findById(queueId)
            ?: throw BizException(HttpStatus.NOT_FOUND, "등록되지 않은 대기열 입니다.")
        queue.pass()

        repo.save(queue)
    }

    @Transactional
    fun deleteTimeout() {
        repo.deleteTimeout()
    }

}