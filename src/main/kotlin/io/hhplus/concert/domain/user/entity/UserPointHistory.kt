package io.hhplus.concert.domain.user.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class PointHistoryType {
    USE, CHARGE, CANCEL
}

@Entity
class UserPointHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    val amount: Long = 0L,

    @Enumerated(EnumType.STRING)
    val type: PointHistoryType = PointHistoryType.USE,

    val createdAt: LocalDateTime = LocalDateTime.now()
)