package io.hhplus.concert.domain.user.dto

import io.hhplus.concert.domain.user.entity.User

class UserResponse(
    val id: Long,
    val name: String
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name
    )
}

