package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.infrastructure.jpa.QueueJpaRepository
import io.hhplus.concert.infrastructure.jpa.ScheduleJpaRepository
import io.hhplus.concert.infrastructure.jpa.UserJpaRepository

class QueueRepositoryImpl(
    private val jpa: QueueJpaRepository,
    private val userJpa: UserJpaRepository,
    private val scheduleJpa: ScheduleJpaRepository
) : QueueRepository {
    override fun save(queue: Queue): Queue =
        jpa.save(queue)

    override fun save(userId: Long, scheduleId: Long): Queue {
        val user = userJpa.findById(userId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 유저입니다.") }
        val schedule = scheduleJpa.findById(scheduleId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 콘서트 일정입니다.") }

        return jpa.save(Queue(user = user, schedule = schedule))
    }

    override fun findById(id: Long): Queue? =
        jpa.findById(id).orElse(null)

    override fun findByUserId(userId: Long): Queue? =
        jpa.findByUserId(userId).orElse(null)

    override fun findAllByScheduleId(scheduleId: Long): List<Queue> =
        jpa.findAllByScheduleId(scheduleId)

    override fun delete(queue: Queue) =
        jpa.delete(queue)

}