package io.hhplus.concert.infrastructure.task

import io.hhplus.concert.infrastructure.jpa.QueueJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QueueScheduler(private val repo: QueueJpaRepository) {

    private val allowedQueueCount = 50

    @Transactional
    @Scheduled(fixedRate = 1000 * 30)
    fun passQueues() {
        val count = repo.findAllPassQueue().count()

        if (allowedQueueCount - count <= 0) return

        val limit = PageRequest.of(0, allowedQueueCount - count)
        val oldest = repo.findOldestWaitingQueueTopBy(limit)
        oldest.forEach { it.pass() }
        repo.saveAll(oldest)
    }

    @Transactional
    @Scheduled(fixedRate = 1000 * 30)
    fun expirePassQueues() {
        repo.findAllPassQueue().filter { it.isTimeOut() }.run { repo.deleteAll(this) }
    }

}