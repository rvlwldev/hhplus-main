package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.pointHistory.PointHistory
import io.hhplus.concert.domain.pointHistory.PointHistoryRepository
import io.hhplus.concert.infrastructure.jpa.PointHistoryJpaRepository
import org.springframework.stereotype.Repository

@Repository
class PointHistoryRepository(private val jpa: PointHistoryJpaRepository) : PointHistoryRepository {
    override fun save(history: PointHistory): PointHistory =
        jpa.save(history)

    override fun findAllByUserId(userId: Long): List<PointHistory> =
        jpa.findAllByUserId(userId)

}