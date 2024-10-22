package io.hhplus.concert.domain.user

class UserResponse(
    val id: Long,
    val name: String,
    val point: Long
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name,
        point = user.point
    )
}

