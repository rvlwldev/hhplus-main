package io.hhplus.concert.domain.queue


import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Transient
import java.time.LocalDateTime

enum class QueueStatus {
    WAIT, PASS
}

@Entity
class Queue(

    @Transient
    val QUEUE_TIME_OUT_SECONDS: Long = 60 * 5,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val userId: Long = 0L,
    val scheduleId: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    updatedAt: LocalDateTime? = null,
    status: QueueStatus = QueueStatus.WAIT
) {
    var updatedAt: LocalDateTime? = null
        protected set

    var status = QueueStatus.WAIT
        protected set

    fun pass() {
        val now = LocalDateTime.now()

        if (createdAt.plusSeconds(QUEUE_TIME_OUT_SECONDS) < now)
            throw BizException(BizError.Queue.TIME_OUT)

        updatedAt = now
        status = QueueStatus.PASS
    }
}



