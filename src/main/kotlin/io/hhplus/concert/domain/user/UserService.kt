package io.hhplus.concert.domain.user

import io.hhplus.concert.core.exception.BizException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service


@Service
class UserService(private val repo: UserRepository) {

    fun save(user: User): UserInfo = UserInfo(repo.save(user))
    fun save(name: String): UserInfo = UserInfo(repo.save(name))

    fun get(id: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        return UserInfo(user)
    }

    fun getPoint(userId: Long) = repo.findById(userId)?.point
        ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun chargePoint(id: Long, amount: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        user.chargePoint(amount)

        return this.save(user)
    }

    fun usePoint(id: Long, amount: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

        user.usePoint(amount)

        return this.save(user)
    }

    companion object {
        private const val NOT_FOUND_MESSAGE: String = "존재하지 않는 유저입니다."
    }

}