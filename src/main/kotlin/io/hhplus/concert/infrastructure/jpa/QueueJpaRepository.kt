package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.queue.Queue
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface QueueJpaRepository : JpaRepository<Queue, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun save(queue: Queue): Queue

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun saveAll(entities: List<Queue>): MutableList<Queue>

    @Query("SELECT q FROM Queue q WHERE q.userId = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByUserId(userId: Long): Optional<Queue>

    @Query("SELECT q FROM Queue q WHERE q.scheduleId = :scheduleId")
    fun findAllByScheduleId(scheduleId: Long): List<Queue>

    @Query(
        "SELECT q FROM Queue q " +
                "WHERE q.status = io.hhplus.concert.domain.queue.QueueStatus.WAIT " +
                "ORDER BY updatedAt"
    )
    fun findOldestWaitingQueueTopBy(pageable: Pageable): List<Queue>

    @Query(
        "SELECT q FROM Queue q " +
                "WHERE q.status = io.hhplus.concert.domain.queue.QueueStatus.PASS" +
                "ORDER BY q.createdAt"
    )
    fun findAllPassQueue(): List<Queue>

    @Query(
        "SELECT q FROM Queue q " +
                "WHERE q.status = io.hhplus.concert.domain.queue.QueueStatus.PASS"
    )
    fun findWaitingQueueTopBy(pageable: Pageable): List<Queue>


}