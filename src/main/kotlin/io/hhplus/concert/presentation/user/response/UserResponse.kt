package io.hhplus.concert.presentation.user.response

import io.hhplus.concert.domain.user.UserInfo

data class UserResponse(
    val id: Long,
    val name: String,
    val point: Long
) {
    constructor(info: UserInfo) : this(
        id = info.id,
        name = info.name,
        point = info.point
    )
}