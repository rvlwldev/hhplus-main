package io.hhplus.concert.domain.concert.repository

import io.hhplus.concert.domain.concert.entity.Concert
import io.hhplus.concert.domain.concert.entity.ConcertSchedule
import io.hhplus.concert.domain.concert.entity.Seat

interface ConcertRepository {

    fun findById(concertId: Long): Concert?

    fun findAll(): List<Concert>

    fun findScheduleById(scheduleId: Long): ConcertSchedule?

    fun findAllScheduleByConcertId(concertId: Long): List<ConcertSchedule>

    fun findAllSeatByScheduleId(scheduleId: Long): List<Seat>

}