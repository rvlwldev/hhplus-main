package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.pointHistory.PointHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PointHistoryJpaRepository : JpaRepository<PointHistory, Long> {

    @Query("SELECT h FROM PointHistory h WHERE h.userId = :userId")
    fun findAllByUserId(userId: Long): List<PointHistory>
}