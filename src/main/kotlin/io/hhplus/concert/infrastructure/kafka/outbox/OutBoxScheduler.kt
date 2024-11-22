package io.hhplus.concert.infrastructure.kafka.outbox

import io.hhplus.concert.infrastructure.implement.PaymentRepositoryImpl
import io.hhplus.concert.infrastructure.implement.SeatRepositoryImpl
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class OutBoxScheduler(
    private val outboxRepo: OutBoxRepositoryImpl,
    private val kafka: KafkaTemplate<String, String>,
    private val seatRepo: SeatRepositoryImpl,
    private val paymentRepo: PaymentRepositoryImpl
) {

    @Scheduled(fixedDelay = 60000)
    fun handlePendedOutBox() {
        outboxRepo.findAllNotDoneOrFail().forEach { outbox ->
            outbox.increaseFailCount()
            kafka.send(outbox.topic, outbox.message)
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    fun handleFailedOutBox() {
        outboxRepo.findAllFail().forEach { outBox ->
            val id = outBox.entityId

            if (outBox.entityName == "Seat")
                seatRepo.findById(id)?.cancel()
            else if (outBox.entityName == "Payment")
                paymentRepo.findById(id)?.cancel()
        }

    }

}