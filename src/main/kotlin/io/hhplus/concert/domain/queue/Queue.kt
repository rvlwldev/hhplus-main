package io.hhplus.concert.domain.queue

import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Queue(

    @Transient
    val QUEUE_TIME_OUT_SECONDS: Long = 60 * 5,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    val schedule: Schedule = Schedule(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    updatedAt: LocalDateTime? = null,

    status: QueueStatus = QueueStatus.WAIT
) {

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: QueueStatus = QueueStatus.WAIT
        protected set

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
        protected set

    fun pass() {
        if (status == QueueStatus.PASS)
            throw IllegalStateException("이미 처리된 대기열 입니다.")

        validateTimeOut()

        status = QueueStatus.PASS
        updatedAt = LocalDateTime.now()
    }

    fun validateTimeOut() {
        val now = LocalDateTime.now()
        val limit = createdAt.plusSeconds(QUEUE_TIME_OUT_SECONDS)

        if (now.isAfter(limit))
            throw IllegalStateException("예약 대기 시간이 만료되었습니다.")
    }

}