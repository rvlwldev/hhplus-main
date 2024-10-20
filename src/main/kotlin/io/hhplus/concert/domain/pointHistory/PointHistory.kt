package io.hhplus.concert.domain.pointHistory

import io.hhplus.concert.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class PointHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @Column(name = "amount")
    val amount: Long = 0L,

    @Enumerated(EnumType.STRING)
    val type: PointHistoryType = PointHistoryType.USE,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)