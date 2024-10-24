package io.hhplus.concert.domain.schedule

import org.springframework.stereotype.Service

@Service
class ScheduleService(private val repo: ScheduleRepository) {

    fun get(id: Long) = repo.findById(id)
        ?.run { ScheduleInfo(this) }
        ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

    fun getAll(concertId: Long) = repo.findAllByConcertId(concertId)
        .map { ScheduleInfo(it) }

    fun getReservableList(concertId: Long) = repo.findAllByConcertId(concertId)
        .filter { it.isReservable() }
        .map { ScheduleInfo(it) }

    fun isReservable(id: Long) = repo.findById(id)?.isReservable()
        ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

    companion object {
        private const val NOT_FOUND_MESSAGE = "존재하지 않는 일정입니다."
    }
}