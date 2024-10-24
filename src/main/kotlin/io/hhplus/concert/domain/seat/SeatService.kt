package io.hhplus.concert.domain.seat

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SeatService(
    private val repo: SeatRepository,
    private val userRepo: UserRepository
) {
    fun get(scheduleId: Long, seatNumber: Long): SeatInfo {
        val seat = repo.findByScheduleIdAndNumber(scheduleId, seatNumber)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        return SeatInfo(seat)
    }

    fun getAll(scheduleId: Long) = repo.findAllByScheduleId(scheduleId)
        .map { SeatInfo(it) }

    fun getAllReservable(scheduleId: Long) = repo.findAllByScheduleId(scheduleId)
        .filter { it.status == SeatStatus.EMPTY }
        .map { SeatInfo(it) }

    fun reserve(scheduleId: Long, userId: Long, seatNumber: Long): SeatInfo {
        val seat = repo.findAllByScheduleId(scheduleId)
            .find { it.seatNumber == seatNumber }
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        val user = userRepo.findById(userId)
            ?: throw BizException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE)

        if (seat.status == SeatStatus.WAIT || seat.status == SeatStatus.PAID)
            throw BizException(HttpStatus.CONFLICT, ALREADY_RESERVED_MESSAGE)

        seat.reserve(user)

        return repo.save(seat)
            .run { SeatInfo(this) }
    }

    fun confirm(scheduleId: Long, userId: Long, seatNumber: Long): SeatInfo {
        val seat = repo.findByScheduleIdAndNumber(scheduleId, seatNumber)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        val user = userRepo.findById(userId)
            ?: throw BizException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE)

        seat.confirm(user)

        return repo.save(seat)
            .run { SeatInfo(this) }
    }

    companion object {
        private const val NOT_FOUND_MESSAGE = "해당 좌석을 찾을 수 없습니다."
        private const val USER_NOT_FOUND_MESSAGE = "존재하지 않는 유저입니다."
        private const val ALREADY_RESERVED_MESSAGE = "이미 선점된 좌석입니다."
    }

}