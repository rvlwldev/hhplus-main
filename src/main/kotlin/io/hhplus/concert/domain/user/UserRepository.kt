package io.hhplus.concert.domain.user

import io.hhplus.concert.domain.user.entity.User
import io.hhplus.concert.domain.user.entity.UserPointHistory

interface UserRepository {
    fun findById(userId: Long): User?
    fun save(user: User): User
    fun findPointHistoryListById(userId: Long): List<UserPointHistory>
    fun saveHistory(history: UserPointHistory): UserPointHistory
}