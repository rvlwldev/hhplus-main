package kafka

import io.hhplus.concert.domain.core.outbox.OutBox
import io.hhplus.concert.domain.payment.Payment
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.infrastructure.implement.PaymentRepositoryImpl
import io.hhplus.concert.infrastructure.implement.SeatRepositoryImpl
import io.hhplus.concert.infrastructure.kafka.outbox.OutBoxJpaRepository
import io.hhplus.concert.infrastructure.kafka.outbox.OutBoxRepositoryImpl
import io.hhplus.concert.infrastructure.kafka.outbox.OutBoxScheduler
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig
@EmbeddedKafka(
    partitions = 1, topics = ["test-topic"],
    brokerProperties = ["listeners=PLAINTEXT://localhost:9001", "port=9002"]
)
class KafkaSchedulerTest {

    private lateinit var kafkaConsumer: KafkaConsumer<String, String>

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker


    @MockBean
    private lateinit var outboxRepo: OutBoxRepositoryImpl

    @MockBean
    private lateinit var outboxJpa: OutBoxJpaRepository

    @MockBean
    private lateinit var seatRepo: SeatRepositoryImpl

    @MockBean
    private lateinit var paymentRepo: PaymentRepositoryImpl

    @MockBean
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    private lateinit var outBoxScheduler: OutBoxScheduler

    @BeforeEach
    fun setup() {
        outBoxScheduler = OutBoxScheduler(outboxRepo, kafkaTemplate, seatRepo, paymentRepo)

        val consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker)
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        kafkaConsumer = KafkaConsumer<String, String>(consumerProps)
        kafkaConsumer.subscribe(listOf("test-topic"))
    }

    private fun mockOutBox(entityName: String, entityId: Long, topic: String, message: String): OutBox {
        val outbox = mock(OutBox::class.java)
        `when`(outbox.topic).thenReturn(topic)
        `when`(outbox.entityId).thenReturn(entityId)
        `when`(outbox.entityName).thenReturn(entityName)
        `when`(outbox.message).thenReturn(message)
        return outbox
    }

    @Test
    fun `카프카 재발행 아웃박스 처리 성공 테스트`() {
        val pendingOutboxes = listOf(
            mockOutBox("Seat", 1L, "test-topic", "message1"), // 메시지 추가
            mockOutBox("Payment", 2L, "test-topic", "message2") // 메시지 추가
        )

        `when`(outboxRepo.findAllNotDoneOrFail()).thenReturn(pendingOutboxes)

        outBoxScheduler.handlePendedOutBox()

        verify(kafkaTemplate).send("test-topic", "message1")
        verify(kafkaTemplate).send("test-topic", "message2")
        verify(outboxRepo).findAllNotDoneOrFail()
    }

    @Test
    fun `실패케이스를 찾고 취소처리 여부 확인`() {
        val seatMock = mock(Seat::class.java)
        val paymentMock = mock(Payment::class.java)

        `when`(seatRepo.findById(1L)).thenReturn(seatMock)
        `when`(paymentRepo.findById(2L)).thenReturn(paymentMock)

        val failedOutboxes = listOf(
            mockOutBox("Seat", 1L, "Seat", "message1"),
            mockOutBox("Payment", 2L, "Payment", "message2")
        )
        `when`(outboxRepo.findAllFail()).thenReturn(failedOutboxes)

        outBoxScheduler.handleFailedOutBox() // 스케줄러 동작

        // 실패 확인
        verify(outboxRepo).findAllFail()

        // 조회 확인
        verify(seatRepo).findById(1L)
        verify(paymentRepo).findById(2L)

        // 취소 확인
        verify(seatMock).cancel()
        verify(paymentMock).cancel()


    }


}