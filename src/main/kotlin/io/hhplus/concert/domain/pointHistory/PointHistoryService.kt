package io.hhplus.concert.domain.pointHistory

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PointHistoryService(
    private val repo: PointHistoryRepository,
    private val userRepo: UserRepository
) {
    fun save(userId: Long, amount: Long, type: PointHistoryType): PointHistoryInfo {
        val user = userRepo.findById(userId)
            ?: throw BizException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE)

        if (amount < 1)
            throw BizException(HttpStatus.BAD_REQUEST, INVALID_AMOUNT_MESSAGE)

        return repo.save(PointHistory(user = user, amount = amount, type = type))
            .run { PointHistoryInfo(this) }
    }

    fun getAll(userId: Long) = repo.findAllByUserId(userId)
        .map { PointHistoryInfo(it) }

    companion object {
        private const val USER_NOT_FOUND_MESSAGE: String = "존재하지 않는 유저입니다."
        private const val INVALID_AMOUNT_MESSAGE: String = "올바르지 않은 금액입니다."
    }
}
