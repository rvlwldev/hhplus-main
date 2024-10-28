package io.hhplus.concert.domain.pointHistory

import org.springframework.stereotype.Service

@Service
class PointHistoryService(private val repo: PointHistoryRepository) {

    fun save(userId: Long, amount: Long, type: PointHistoryType) = repo.save(PointHistory(userId, amount, type = type))
        .run { PointHistoryInfo(this) }

    fun getAll(userId: Long) = repo.findAllByUserId(userId)
        .map { PointHistoryInfo(it) }

}
