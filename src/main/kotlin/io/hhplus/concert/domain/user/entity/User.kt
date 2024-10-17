package io.hhplus.concert.domain.user.entity

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.dto.PointResponse
import io.hhplus.concert.domain.user.dto.UserResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.http.HttpStatus

@Entity
class User(
    @Id
    val userId: Long = 0L,

    point: Long = 0L
) {
    var point = 0L
        protected set

    fun charge(amount: Long): User {
        if (amount < 1) throw BizException(HttpStatus.BAD_REQUEST, "올바르지 않은 충전금액 입니다.")
        point += amount
        return this
    }

    fun toDTO() = UserResponse(userId)

    fun toPointDTO() = PointResponse(point)
}
