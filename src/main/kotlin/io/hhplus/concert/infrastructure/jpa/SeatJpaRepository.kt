package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.seat.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaRepository : JpaRepository<Seat, Long> {
}