package io.hhplus.concert.domain.payment.event

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.domain.core.outbox.OutBox
import io.hhplus.concert.domain.core.outbox.OutBoxService
import io.hhplus.concert.domain.core.outbox.ProgressStatus
import io.hhplus.concert.domain.seat.Seat
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentEventListener(
    private val kafka: KafkaTemplate<String, String>,
    private val outboxService: OutBoxService,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutBox(payment: PaymentEvent.Init) {
        val outbox = OutBox.create<Seat>(
            "\${topic.seat.pay}",
            payment.id,
            ProgressStatus.INIT,
            payment.toString()
        )

        outboxService.save(outbox)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendPay(payment: PaymentEvent.Init) {
        val paymentJson = objectMapper.writeValueAsString(payment)
        kafka.send("\${topic.seat.pay}", paymentJson)
    }

}