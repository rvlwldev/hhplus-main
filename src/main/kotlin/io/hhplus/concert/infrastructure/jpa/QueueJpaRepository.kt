package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.queue.Queue
import org.springframework.data.jpa.repository.JpaRepository

interface QueueJpaRepository : JpaRepository<Queue, Long> {
}