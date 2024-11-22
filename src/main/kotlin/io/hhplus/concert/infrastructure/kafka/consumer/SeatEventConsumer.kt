package io.hhplus.concert.infrastructure.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.domain.seat.event.SeatEvent
import io.hhplus.concert.infrastructure.kafka.outbox.OutBoxJpaRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SeatEventConsumer(
    private val outboxRepository: OutBoxJpaRepository,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["\${topic.seat.reserve}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun reserve(record: ConsumerRecord<String, String>) {
        val seat = objectMapper.readValue(record.value(), SeatEvent.Init::class.java)
        val outbox = outboxRepository.findEvent("\${topic.seat.reserve}", seat.id, "Seat")
            ?: throw IllegalArgumentException()

        outbox.progress()

        outboxRepository.save(outbox)
    }

    @KafkaListener(topics = ["\${topic.seat.pay}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun confirm(record: ConsumerRecord<String, String>) {
        val seat = objectMapper.readValue(record.value(), SeatEvent.Confirm::class.java)
        val outbox = outboxRepository.findEvent("\${topic.seat.reserve}", seat.id, "Seat")
            ?: throw IllegalArgumentException()

        outbox.done()

        outboxRepository.save(outbox)
    }

}