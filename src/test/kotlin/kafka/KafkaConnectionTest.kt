package kafka

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaConnectionTest {

    private lateinit var kafkaContainer: KafkaContainer

    private lateinit var producerProps: Properties
    private lateinit var consumerProps: Properties

    private val TOPIC = "test-topic"

    @BeforeAll
    fun setUp() {
        // KafkaContainer 초기화 및 시작
        kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
        kafkaContainer.start()

        // Producer 설정
        producerProps = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.bootstrapServers)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        }

        // Consumer 설정
        consumerProps = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.bootstrapServers)
            put(ConsumerConfig.GROUP_ID_CONFIG, "test-group")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        }

        createTopic(TOPIC)
    }

    private fun KafkaContainer.createAdminClient(): AdminClient {
        val props = Properties().apply {
            put("bootstrap.servers", this@createAdminClient.bootstrapServers)
        }
        return AdminClient.create(props)
    }

    private fun createTopic(topic: String) {
        val adminClient = kafkaContainer.createAdminClient()
        adminClient.createTopics(listOf(org.apache.kafka.clients.admin.NewTopic(topic, 1, 1))).all().get()
        adminClient.close()
    }

    @AfterAll
    fun tearDown() {
        kafkaContainer.stop()
    }

    @Test
    fun `카프카 이벤트 발행 소비 테스트`() {
        KafkaProducer<String, String>(producerProps).use { producer ->
            producer.send(ProducerRecord(TOPIC, "key", "message")).get()
        }

        KafkaConsumer<String, String>(consumerProps).use { consumer ->
            consumer.subscribe(listOf(TOPIC))
            val records = consumer.poll(Duration.ofSeconds(10))
            Assertions.assertEquals(1, records.count())
            Assertions.assertEquals("message", records.first().value())
        }
    }
}