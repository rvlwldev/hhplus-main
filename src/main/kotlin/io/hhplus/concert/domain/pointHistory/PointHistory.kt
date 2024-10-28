package io.hhplus.concert.domain.pointHistory

import io.hhplus.concert.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

enum class PointHistoryType {
    USE, CHARGE, CANCEL
}

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