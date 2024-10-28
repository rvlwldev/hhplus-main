package io.hhplus.concert.application.reservation

import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationFacade(
    private val tokenManager: TokenManager,
    private val userService: UserService,
    private val scheduleService: ScheduleService,
    private val queueService: QueueService,
    private val paymentService: PaymentService,
) {
    @Transactional
    fun reserve(userId: Long, scheduleId: Long): ReservationResult {
        val user = userService.get(userId)
        val schedule = scheduleService.get(scheduleId)

        val queue = queueService.create(user.id, schedule.id)
        val token = tokenManager.createQueueToken(queue.id, user.id, schedule.id, queue.status)

        return ReservationResult(token, TokenManager.Type.RESERVATION, schedule.id)
    }

    @Transactional
    fun getStatus(token: String): ReservationResult {
        val claims = tokenManager.validateQueueToken(token)

        val id = claims.getValue("id") as Long
        val userId = claims.getValue("userId") as Long
        val scheduleId = claims.getValue("scheduleId") as Long
        val status = claims.getValue("status") as String

        val queue = queueService.get(id)

        return if (queue.status === status)
            ReservationResult(token, TokenManager.Type.RESERVATION, scheduleId)
        else paymentService.create(userId)
            .let { payment -> tokenManager.createPaymentToken(userId, scheduleId, payment.id) }
            .run { ReservationResult(this, TokenManager.Type.PAYMENT, scheduleId) }
    }

}