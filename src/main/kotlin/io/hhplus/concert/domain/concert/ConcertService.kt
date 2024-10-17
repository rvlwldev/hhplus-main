package io.hhplus.concert.domain.concert

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.entity.SeatStatus
import io.hhplus.concert.domain.concert.repository.ConcertRepository
import io.hhplus.concert.domain.concert.repository.SeatRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConcertService(
    private val repo: ConcertRepository,
    private val seatRepo: SeatRepository
) {

    fun get(concertId: Long) = repo.findScheduleById(concertId)
        ?: BizException(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다.")

    fun getAll() = repo.findAll().map { it.toDTO() }

    fun getSchedule(scheduleId: Long) = repo.findScheduleById(scheduleId)?.toDTO()
        ?: BizException(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다.")

    fun getScheduleList(concertId: Long) = repo.findAllScheduleByConcertId(concertId).map { it.toDTO() }

    @Transactional
    fun reserveSeat(userId: Long, scheduleId: Long, seatId: Long) {
        val seat = seatRepo.findWithLock(scheduleId, seatId)
            ?: throw BizException(HttpStatus.NOT_FOUND, "해당 좌석을 찾을 수 없습니다.")

        if (seat.status != SeatStatus.EMPTY) {
            throw BizException(HttpStatus.CONFLICT, "이미 예약된 좌석입니다.")
        }

        seat.userId = userId
        seat.reserve()
        seatRepo.save(seat)
    }


    @Transactional
    fun confirmSeat(userId: Long, scheduleId: Long, seatId: Long) {
        val seat = seatRepo.findWithLock(scheduleId, seatId)
            ?: throw BizException(HttpStatus.NOT_FOUND, "해당 좌석을 찾을 수 없습니다.")

        if (seat.status != SeatStatus.WAIT || seat.userId != userId)
            throw BizException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")

        seat.confirm()
        seatRepo.save(seat)
    }

    @Transactional
    fun resetSeatStatus(userId: Long, scheduleId: Long) {
        val seat = seatRepo.findByUserIdAndScheduleIdWithLock(userId, scheduleId)
        if (seat != null && seat.status == SeatStatus.RESERVED) {
            seat.cancel()
            seatRepo.save(seat)
        }
        throw BizException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
    }
}