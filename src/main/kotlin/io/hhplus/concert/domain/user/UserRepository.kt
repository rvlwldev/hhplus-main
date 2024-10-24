package io.hhplus.concert.domain.user

interface UserRepository {
    fun save(name: String): User
    fun save(user: User): User

    fun findById(id: Long): User?
}