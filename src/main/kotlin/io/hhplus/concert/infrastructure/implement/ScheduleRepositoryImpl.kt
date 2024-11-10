package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleRepository
import io.hhplus.concert.infrastructure.jpa.ScheduleJpaRepository
import io.hhplus.concert.infrastructure.redis.ScheduleRedisRepository
import org.springframework.stereotype.Repository

@Repository
class ScheduleRepositoryImpl(
    private val jpa: ScheduleJpaRepository,
    private val redis: ScheduleRedisRepository
) : ScheduleRepository {

    override fun save(schedule: Schedule): Schedule =
        jpa.save(schedule)

    override fun findById(id: Long): Schedule? =
        redis.find(id) ?: jpa.findById(id).orElse(null)
            ?.also { redis.save(it) }

    override fun findAllByConcertId(concertId: Long): List<Schedule> =
        redis.findAll(concertId)
            .takeIf { it.isNotEmpty() } ?: jpa.findAllByConcertId(concertId)
            .also { redis.saveAll(it) }

}