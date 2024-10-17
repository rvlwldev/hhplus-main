package io.hhplus.concert.domain.queue

import jakarta.persistence.*
import java.time.ZoneId
import java.time.ZonedDateTime

data class QueueInfo(
    val queueId: Long,
    val userId: Long,
    val scheduleId: Long,
    val status: String,
    val createdAt: ZonedDateTime
)

enum class QueueStatus {
    WAIT, PASS
}

@Entity
class Queue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val queueId: Long = 0L,
    val userId: Long = 0L,
    val scheduleId: Long = 0L,

    @Enumerated(EnumType.STRING)
    var status: QueueStatus = QueueStatus.WAIT,

    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")),
    private var updatedAt: ZonedDateTime? = null
) {

    fun toDTO() = QueueInfo(queueId, userId, scheduleId, status = status.name, createdAt)

    fun pass() {
        status = QueueStatus.PASS
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    }
}