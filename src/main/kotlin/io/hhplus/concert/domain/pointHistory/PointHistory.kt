package io.hhplus.concert.domain.pointHistory

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

enum class PointHistoryType {
    USE, CHARGE, CANCEL
}

@Entity
class PointHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val userId: Long = 0L,

    @Column(name = "amount")
    val amount: Long = 0L,

    @Enumerated(EnumType.STRING)
    val type: PointHistoryType = PointHistoryType.USE,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)