package io.hhplus.concert.domain.pointHistory

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class PointHistoryService(
    private val repo: PointHistoryRepository,
    private val userRepo: UserRepository
) {
    fun save(userId: Long, amount: Long, type: PointHistoryType): PointHistoryInfo {
        val user = userRepo.findById(userId)
            ?: throw BizException(BizError.User.NOT_FOUND)

        when (type) {
            PointHistoryType.USE -> user.usePoint(amount)
            PointHistoryType.CHARGE -> user.chargePoint(amount)
            else -> throw BizException(BizError.Payment.FAILED)
        }

        return repo.save(PointHistory(user = user, amount = amount, type = type))
            .run { PointHistoryInfo(this) }
    }

    fun getAll(userId: Long) = repo.findAllByUserId(userId)
        .map { PointHistoryInfo(it) }
}
