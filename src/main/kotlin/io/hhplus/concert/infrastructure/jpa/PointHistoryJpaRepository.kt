package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.pointHistory.PointHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PointHistoryJpaRepository : JpaRepository<PointHistory, Long> {
}