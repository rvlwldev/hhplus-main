package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.concert.Concert
import org.springframework.data.jpa.repository.JpaRepository

interface ConcertJpaRepository : JpaRepository<Concert, Long> {
}