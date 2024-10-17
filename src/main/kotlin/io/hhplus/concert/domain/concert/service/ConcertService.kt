package io.hhplus.concert.domain.concert.service

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ConcertService(private val repo: ConcertRepository) {
    private val NOT_FOUND_MESSAGE = "콘서트를 찾을 수 없습니다."

    fun get(concertId: Long) = repo.findScheduleById(concertId)
        ?: BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun getAll() = repo.findAll().map { it.toDTO() }

    fun getSchedule(scheduleId: Long) = repo.findScheduleById(scheduleId)?.toDTO()
        ?: BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun getScheduleList(concertId: Long) = repo.findAllScheduleByConcertId(concertId).map { it.toDTO() }

    fun getReservableSeatList(scheduleId: Long): List<Long> {
        repo.findScheduleById(scheduleId) ?: BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)
        return repo.findAllSeatByScheduleId(scheduleId)
            .filter { it.isAvailable() }
            .map { it.toLong() }
    }


}