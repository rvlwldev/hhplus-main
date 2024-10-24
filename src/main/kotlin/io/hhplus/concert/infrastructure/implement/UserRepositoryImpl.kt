package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.user.User
import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.infrastructure.jpa.UserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val jpa: UserJpaRepository) : UserRepository {
    override fun save(name: String): User =
        jpa.save(User(name = name))

    override fun save(user: User): User =
        jpa.save(user)

    override fun findById(id: Long): User? =
        jpa.findById(id).orElse(null)
}