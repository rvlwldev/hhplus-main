package io.hhplus.concert.domain.queue

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueueSchedulerService(
    private val service: QueueService
) {

    @Scheduled(fixedRate = 60000)
    @Transactional
    fun queuePass() {
        val size = 30
        val queues = service.getAllWaitQueueBySize(size = size)

        queues.forEach { service.pass(it.queueId) }
    }

    @Scheduled(fixedRate = 30000)
    fun deleteTimeoutQueues() {
        service.deleteTimeout()
    }

    @Scheduled(fixedRate = 600000)
    fun deletePayTimeout() {
        service.deletePaymentTimeout()
    }

}
