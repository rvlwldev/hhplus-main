package io.hhplus.concert.infrastructure.queue

import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.domain.queue.QueueStatus
import jakarta.persistence.LockModeType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

interface QueueJpaRepository : JpaRepository<Queue, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userId = :userId AND q.scheduleId = :scheduleId")
    fun findByUserIdAndScheduleId(userId: Long, scheduleId: Long): Optional<Queue>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.status = 'WAIT' ORDER BY q.createdAt ASC")
    fun findWaitQueueByWithLockAndSize(limit: Pageable): List<Queue>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userId = :userId AND q.scheduleId = :scheduleId AND q.status = 'PASS'")
    fun findPassQueueByUserIdAndScheduleIdWithLock(userId: Long, scheduleId: Long): Optional<Queue>

    @Modifying
    @Query("DELETE FROM Queue q WHERE q.updatedAt < :expiredTime AND q.status = 'PASS'")
    fun deleteAllTimeout(expiredTime: ZonedDateTime)

    // In QueueRepository interface

    @Modifying
    @Query("DELETE FROM Queue q WHERE q.userId = :userId AND q.scheduleId = :scheduleId")
    fun deleteByUserIdAndScheduleId(userId: Long, scheduleId: Long): Int

    @Modifying
    @Query("DELETE FROM Queue q WHERE q.status = :status AND q.updatedAt < :updatedAtBefore")
    fun deleteByStatusAndUpdatedAtBefore(
        status: QueueStatus,
        updatedAtBefore: ZonedDateTime
    ): Int
}

@Repository
class QueueRepositoryImpl(private val jpa: QueueJpaRepository) : QueueRepository {
    override fun findById(queueId: Long): Queue? = jpa.findById(queueId).orElse(null)

    override fun findByUserIdAndScheduleId(userId: Long, scheduleId: Long): Queue? =
        jpa.findByUserIdAndScheduleId(userId, scheduleId).orElse(null)

    override fun findWaitQueueWithSize(size: Int): List<Queue> {
        val pageable: Pageable = PageRequest.of(0, size)
        return jpa.findWaitQueueByWithLockAndSize(pageable)
    }

    override fun findPassQueueByUserIdAndScheduleId(userId: Long, scheduleId: Long): Queue? =
        jpa.findPassQueueByUserIdAndScheduleIdWithLock(userId, scheduleId).orElse(null)

    override fun save(queue: Queue) = jpa.save(queue)

    override fun deleteTimeout() {
        val expiredTime = ZonedDateTime.now().minusMinutes(10) // 10분 지난 대기열 삭제
        jpa.deleteAllTimeout(expiredTime)
    }

    override fun deleteByUserIdAndScheduleId(userId: Long, scheduleId: Long) {
        jpa.deleteByUserIdAndScheduleId(userId, scheduleId)
    }

    override fun deleteByStatusAndUpdatedAtBefore(status: QueueStatus, updatedAtBefore: ZonedDateTime) {
        TODO("Not yet implemented")
    }

}