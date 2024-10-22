package io.hhplus.concert.domain.user

import org.springframework.stereotype.Service


@Service
class UserService(private val repo: UserRepository) {

    private final val NOT_FOUND_MESSAGE: String = "존재하지 않는 유저입니다."

    fun save(user: User): UserResponse = UserResponse(repo.save(user))
    fun save(name: String): UserResponse = UserResponse(repo.save(name))

    fun get(id: Long): UserResponse {
        val user = repo.findById(id)
            ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

        return UserResponse(user)
    }

    fun chargePoint(id: Long, amount: Long): UserResponse {
        val user = repo.findById(id)
            ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

        user.chargePoint(amount)

        return this.save(user)
    }

    fun usePoint(id: Long, amount: Long): UserResponse {
        val user = repo.findById(id)
            ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

        user.usePoint(amount)

        return this.save(user)
    }

}