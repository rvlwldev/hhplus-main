package io.hhplus.concert.domain.queue

import org.springframework.stereotype.Service

@Service
class QueueService(private val repo: QueueRepository) {

    fun create(userId: Long, scheduleId: Long) = repo.save(userId, scheduleId)
        .run { QueueInfo(this) }

    fun getByUserId(userId: Long) = repo.findByUserId(userId)
        ?.run { QueueInfo(this) }

    fun getAllByScheduleId(scheduleId: Long) = repo.findAllByScheduleId(scheduleId)
        .map { QueueInfo(it) }

    fun delete(queueId: Long) {
        val queue = repo.findById(queueId)
            ?: throw IllegalArgumentException(Companion.NOT_FOUND_MESSAGE)

        repo.delete(queue)
    }

    companion object {
        private const val NOT_FOUND_MESSAGE = "존재하지 않는 대기열입니다."
    }

}