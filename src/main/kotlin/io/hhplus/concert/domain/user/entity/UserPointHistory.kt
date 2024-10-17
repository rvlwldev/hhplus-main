package io.hhplus.concert.domain.user.entity

import io.hhplus.concert.domain.user.dto.PointHistoryResponse
import jakarta.persistence.*

enum class PointHistoryType {
    CHARGE, USE
}

@Entity
class UserPointHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val historyId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,

    val type: PointHistoryType
) {

    fun toDTO() = PointHistoryResponse(this)
    
}