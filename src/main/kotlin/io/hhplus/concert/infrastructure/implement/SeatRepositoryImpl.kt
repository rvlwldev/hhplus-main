package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.infrastructure.jpa.SeatJpaRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class SeatRepositoryImpl(
    private val jpa: SeatJpaRepository,
    private val listCache: RedisTemplate<String, List<Seat>>,
    private val monoCache: RedisTemplate<String, Seat>,
) : SeatRepository {

    override fun save(seat: Seat): Seat =
        jpa.save(seat)
            .also { monoCache.opsForValue().set("SEAT::$it.id", it, 1, TimeUnit.DAYS) }

    override fun findById(id: Long): Seat? =
        jpa.findById(id).orElse(null)

    override fun findByScheduleIdAndNumber(scheduleId: Long, number: Long): Seat? =
        jpa.findByScheduleIdAndNumber(scheduleId, number).orElse(null)

    override fun findAllByScheduleId(scheduleId: Long): List<Seat> {
        val cache = listCache.opsForValue().get("SEATS::$scheduleId")
        if (cache != null) return cache

        val seats = jpa.findAllByScheduleId(scheduleId)
        listCache.opsForValue().set("SEATS::$scheduleId", seats, 1, TimeUnit.DAYS)

        return seats
    }

}