package io.hhplus.concert.infrastructure.user

import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.domain.user.entity.User
import io.hhplus.concert.domain.user.entity.UserPointHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

interface UserJpaRepository : JpaRepository<User, Long>

interface UserPointHistoryJpaRepository : JpaRepository<UserPointHistory, Long> {
    @Query("SELECT h FROM UserPointHistory h WHERE h.user.userId = :userId")
    fun findAllByUserId(userId: Long): List<UserPointHistory>
}

@Repository
class UserRepositoryImpl(
    private val jpa: UserJpaRepository,
    private val historyJpa: UserPointHistoryJpaRepository
) : UserRepository {

    override fun findById(userId: Long): User? = jpa.findById(userId).orElse(null)

    override fun findPointHistoryListById(userId: Long) = historyJpa.findAllByUserId(userId)

    override fun save(user: User) = jpa.save(user)

    override fun saveHistory(history: UserPointHistory): UserPointHistory = historyJpa.save(history)

}