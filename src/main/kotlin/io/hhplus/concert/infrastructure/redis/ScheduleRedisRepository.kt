package io.hhplus.concert.infrastructure.redis

import io.hhplus.concert.domain.schedule.Schedule
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class ScheduleRedisRepository(private val redis: RedisTemplate<String, String>) {

    private val TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd.-HH:mm")

    private val hash = redis.opsForHash<String, String>()

    private fun scheduleToMap(schedule: Schedule): Map<String, String> = mapOf(
        "id" to schedule.id.toString(),
        "concertId" to schedule.concertId.toString(),
        "sttAt" to schedule.sttAt.format(TIME_FORMAT),
        "endAt" to schedule.endAt.format(TIME_FORMAT),
        "sttReserveAt" to schedule.sttReserveAt.format(TIME_FORMAT),
        "endReserveAt" to schedule.endReserveAt.format(TIME_FORMAT),
        "maximumReservableCount" to schedule.maximumReservableCount.toString(),
        "reservableCount" to schedule.reservableCount.toString()
    )

    private fun mapToSchedule(entries: Map<String, String>): Schedule? {
        return Schedule(
            id = entries["id"]?.toLongOrNull() ?: return null,
            concertId = entries["concertId"]?.toLongOrNull() ?: return null,
            sttAt = entries["sttAt"]?.let { LocalDateTime.parse(it, TIME_FORMAT) }!!,
            endAt = entries["endAt"]?.let { LocalDateTime.parse(it, TIME_FORMAT) }!!,
            sttReserveAt = entries["sttReserveAt"]?.let { LocalDateTime.parse(it, TIME_FORMAT) }!!,
            endReserveAt = entries["endReserveAt"]?.let { LocalDateTime.parse(it, TIME_FORMAT) }!!,
            maximumReservableCount = entries["maximumReservableCount"]?.toLongOrNull() ?: 0L,
            reservableCount = entries["reservableCount"]?.toLongOrNull() ?: 0L
        )
    }

    fun save(schedule: Schedule): Schedule? {
        val key = "SCHEDULE::${schedule.concertId}::${schedule.id}"
        val ttl = Duration.between(LocalDateTime.now(), schedule.endAt).seconds

        hash.putAll(key, scheduleToMap(schedule))
        redis.expire(key, Duration.ofSeconds(ttl))

        return find(schedule.id)
    }

    fun saveAll(schedules: List<Schedule>): List<Schedule> {
        schedules.map { schedule ->
            val key = "SCHEDULE::${schedule.id}"
            val ttl = Duration.between(LocalDateTime.now(), schedule.endAt).seconds

            hash.putAll(key, scheduleToMap(schedule))
            redis.expire(key, Duration.ofSeconds(ttl))

            schedule
        }.mapNotNull { schedule -> find(schedule.id) }
        return schedules
            .mapNotNull { schedule -> find(schedule.id) }
    }

    fun find(scheduleId: Long): Schedule? {
        val entries = hash.entries("SCHEDULE::$scheduleId")
        return if (entries.isEmpty()) null else mapToSchedule(entries)
    }

    fun findAll(): List<Schedule> {
        val keys = redis.keys("SCHEDULE::*")
        return keys.mapNotNull { key -> mapToSchedule(hash.entries(key)) }
    }

    fun findAll(concertId: Long) =
        findAll().filter { it.concertId == concertId }

}
