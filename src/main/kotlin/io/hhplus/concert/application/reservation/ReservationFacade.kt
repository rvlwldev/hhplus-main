package io.hhplus.concert.application.reservation

import io.hhplus.concert.config.support.TokenManager
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
) {

    @Transactional
    fun reserve(criteria: ReservationCriteria): ReservationResult {
        val user = userService.get(criteria.userId)
        val schedule = scheduleService.get(criteria.scheduleId)
        val queue = queueService.create(criteria.userId, criteria.scheduleId)
        val token = tokenManager.createQueueToken(user.id, schedule.scheduleId)

        return ReservationResult(token, TokenManager.Type.RESERVATION, queue)
    }

    @Transactional
    fun getStatus(token: String): ReservationResult {
        val claims = tokenManager.validateQueueToken(token)
        val userId = claims.getValue("userId")
        val scheduleId = claims.getValue("scheduleId")
        val queue = queueService.getByUserId(userId)

        return if (queue.rank < 1) {
            val paymentToken = tokenManager.createPaymentToken(userId, scheduleId)
            ReservationResult(paymentToken, TokenManager.Type.PAYMENT, queue)
        } else ReservationResult(token, TokenManager.Type.RESERVATION, queue)
    }

}