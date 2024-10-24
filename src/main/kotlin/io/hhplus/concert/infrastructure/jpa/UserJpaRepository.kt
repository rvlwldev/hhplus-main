package io.hhplus.concert.infrastructure.jpa

import io.hhplus.concert.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {}