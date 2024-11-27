package io.hhplus.concert.domain.core.outbox

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Transient
import java.time.LocalDateTime


enum class ProgressStatus {
    INIT, PROGRESS, DONE, FAIL
}

@Entity
class OutBox(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val topic: String,

    val entityId: Long = 0,

    val entityName: String = "",

    val message: String = "",

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var status: ProgressStatus = ProgressStatus.INIT,

    var failCount: Int = 0,

    @Transient
    private val maximumFailCount: Int = 5

) {

    fun increaseFailCount() {
        if (status == ProgressStatus.FAIL) throw IllegalStateException()

        failCount++
        if (failCount > maximumFailCount) fail()
    }

    fun progress() {
        status = ProgressStatus.PROGRESS
    }

    fun done() {
        status = ProgressStatus.DONE
    }

    fun fail() {
        status = ProgressStatus.FAIL
    }

    companion object {
        inline fun <reified T> create(topic: String, entityId: Long, message: String) = OutBox(
            entityId = entityId,
            entityName = T::class.java.simpleName,
            topic = topic,
            status = ProgressStatus.INIT
        )

        inline fun <reified T> create(topic: String, entityId: Long, status: ProgressStatus, message: String) = OutBox(
            entityId = entityId,
            entityName = T::class.java.simpleName,
            topic = topic,
            status = status
        )
    }
}