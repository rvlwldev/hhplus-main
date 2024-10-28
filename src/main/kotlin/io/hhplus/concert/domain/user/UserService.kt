package io.hhplus.concert.domain.user

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository) {

    fun save(user: User): UserInfo = UserInfo(repo.save(user))
    fun save(name: String): UserInfo = UserInfo(repo.save(name))

    fun get(id: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(BizError.User.NOT_FOUND)
        return UserInfo(user)
    }

    fun getPoint(userId: Long) = repo.findById(userId)?.point
        ?: throw BizException(BizError.User.NOT_FOUND)

    fun chargePoint(id: Long, amount: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(BizError.User.NOT_FOUND)
        user.chargePoint(amount)
        return this.save(user)
    }

    fun usePoint(id: Long, amount: Long): UserInfo {
        val user = repo.findById(id)
            ?: throw BizException(BizError.User.NOT_FOUND)
        user.usePoint(amount)
        return this.save(user)
    }
}
