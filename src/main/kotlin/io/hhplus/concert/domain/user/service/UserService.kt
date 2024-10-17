package io.hhplus.concert.domain.user.service

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.domain.user.dto.PointResponse
import io.hhplus.concert.domain.user.entity.PointHistoryType
import io.hhplus.concert.domain.user.entity.UserPointHistory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repo: UserRepository
) {
    private val NOT_FOUND_MESSAGE = "유저를 찾을 수 없습니다."

    fun get(userId: Long) = repo.findById(userId)
        ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun getPoint(userId: Long): PointResponse =
        repo.findById(userId)?.toPointDTO()
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun getHistoryList(userId: Long) = repo.findPointHistoryListById(userId)

    @Transactional
    fun chargePoint(userId: Long, amount: Long): PointResponse {
        val user = repo.findById(userId)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        repo.saveHistory(UserPointHistory(user = user, type = PointHistoryType.CHARGE))

        return repo.save(user.charge(amount))
            .toPointDTO()
    }

}