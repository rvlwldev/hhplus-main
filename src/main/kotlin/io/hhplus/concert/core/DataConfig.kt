package io.hhplus.concert.core

import io.hhplus.concert.domain.concert.Concert
import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.infrastructure.jpa.ConcertJpaRepository
import io.hhplus.concert.infrastructure.jpa.ScheduleJpaRepository
import io.hhplus.concert.infrastructure.jpa.SeatJpaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import kotlin.random.Random

@Configuration
class DataConfig(
    private val concertRepository: ConcertJpaRepository,
    private val scheduleRepository: ScheduleJpaRepository,
    private val seatRepository: SeatJpaRepository
) {

    @Bean
    fun initializeData(): CommandLineRunner {
        return CommandLineRunner {
            if (concertRepository.count() == 0L) initConcerts()
            if (scheduleRepository.count() == 0L) initSchedules()
//            if (seatRepository.count() == 0L) initSeats()
        }
    }

    private fun initConcerts() {
        val batchSize = 1000
        val totalConcerts = 100_000

        for (i in 0 until totalConcerts step batchSize) {
            val concerts = List(batchSize) { index ->
                val seatCount = Random.nextLong(50, 500)
                Concert(
                    name = "Dummy Concert ${i + index + 1}",
                    price = Random.nextLong(50_000, 200_000),
                    maximumAudienceCount = seatCount,
                )
            }
            concertRepository.saveAll(concerts)
        }
    }

    private fun initSchedules() {
        val concerts = concertRepository.findAll()
        val schedulesPerConcert = 5

        concerts.forEach { concert ->
            val schedules = List(schedulesPerConcert) { index ->
                val startAt = LocalDateTime.now().plusDays(index.toLong())
                Schedule(
                    concertId = concert.id,
                    sttAt = startAt,
                    endAt = startAt.plusHours(2),
                    sttReserveAt = startAt.minusWeeks(1),
                    endReserveAt = startAt.minusDays(1),
                    maximumReservableCount = concert.maximumAudienceCount,
                    reservableCount = concert.maximumAudienceCount
                )
            }
            scheduleRepository.saveAll(schedules)
        }
    }

    private fun initSeats() {
        val schedules = scheduleRepository.findAll()

        schedules.forEach { schedule ->
            val seats = List(schedule.maximumReservableCount.toInt()) { index ->
                Seat(
                    scheduleId = schedule.id,
                    seatNumber = (index + 1).toLong()
                )
            }
            seatRepository.saveAll(seats)
        }
    }
}