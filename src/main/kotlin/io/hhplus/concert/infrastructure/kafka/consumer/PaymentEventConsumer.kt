package io.hhplus.concert.infrastructure.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.domain.payment.event.PaymentEvent
import io.hhplus.concert.infrastructure.kafka.outbox.OutBoxJpaRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class PaymentEventConsumer(
    private val outboxRepository: OutBoxJpaRepository,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["\${topic.seat.pay}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun pay(record: ConsumerRecord<String, String>) {
        val payment = objectMapper.readValue(record.value(), PaymentEvent.Init::class.java)
        val outbox = outboxRepository.findEvent("\${topic.seat.pay}", payment.id, "Payment")
            ?: throw IllegalArgumentException()

        outbox.progress()

        outboxRepository.save(outbox)
    }

}

