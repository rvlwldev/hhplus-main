package io.hhplus.concert.application.reservation

import io.hhplus.concert.application.reservation.dto.ReservationResult
import io.hhplus.concert.application.support.DistributedLocker
import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class ReservationFacade(
    private val tokenManager: TokenManager,
    private val locker: DistributedLocker,
    private val userService: UserService,
    private val concertService: ConcertService,
    private val scheduleService: ScheduleService,
    private val queueService: QueueService,
    private val paymentService: PaymentService,
    private val transactor: TransactionTemplate
) {

    fun reserve(userId: Long, scheduleId: Long): ReservationResult {
        val lockKey = "reservation:lock:$scheduleId"
        var lockVal: String? = null

        val maxRetries = 5
        val retryDelay = 1000L * 5L
        var retryCount = 0

        while (lockVal == null && retryCount < maxRetries) {
            lockVal = locker.tryLock(lockKey)

            if (lockVal == null) {
                retryCount++
                Thread.sleep(retryDelay)
            }
        }

        if (lockVal == null)
            throw BizException(BizError.Schedule.TOO_MANY_REQUEST)

        try {
            return transactor.execute {
                val user = userService.get(userId)
                val schedule = scheduleService.get(scheduleId)

                val queue = queueService.create(user.id, schedule.id)
//                val token = tokenManager.createQueueToken(queue.id, user.id, schedule.id, queue.status)

                return@execute ReservationResult("token", TokenManager.Type.RESERVATION, schedule.id)
            } ?: throw BizException(HttpStatus.INTERNAL_SERVER_ERROR, "대기열에 진입할 수 없습니다.")
        } finally {
            locker.releaseLock(lockKey, lockVal)
        }
    }

    fun getStatus(token: String): ReservationResult {
        val claims = tokenManager.validateQueueToken(token)

        val id = claims.getValue("id") as Long
        val userId = claims.getValue("userId") as Long
        val scheduleId = claims.getValue("scheduleId") as Long
        val status = claims.getValue("status") as String

        val schedule = scheduleService.get(scheduleId)
        val concert = concertService.get(schedule.id)
        val queue = queueService.get(id)

        return if (queue.status === status)
            ReservationResult(token, TokenManager.Type.RESERVATION, scheduleId)
        else paymentService.create(userId, concert.price) // 대기중인 대기열의 상태가 달라졌을때 Pass
            .let { payment -> tokenManager.createPaymentToken(userId, scheduleId, payment.id) }
            .run { ReservationResult(this, TokenManager.Type.PAYMENT, scheduleId) }
    }

}