package io.hhplus.concert.domain.user

class UserResponse(
    val id: Long,
    val name: String
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name
    )
}

