package io.hhplus.concert.domain.schedule

interface ScheduleRepository {
    fun save(schedule: Schedule): Schedule

    fun findById(id: Long): Schedule?
    fun findAllByConcertId(concertId: Long): List<Schedule>
}