package io.hhplus.concert.domain.pointHistory

interface PointHistoryRepository {
    fun save(history: PointHistory): PointHistory

    fun findAllByUserId(userId: Long): List<PointHistory>
}