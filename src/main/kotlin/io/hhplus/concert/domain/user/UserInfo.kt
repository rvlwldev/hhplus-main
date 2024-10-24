package io.hhplus.concert.domain.user

data class UserInfo(
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

