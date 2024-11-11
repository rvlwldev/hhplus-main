package io.hhplus.concert.application.reservation

import io.hhplus.concert.application.reservation.dto.ReservationResult
import io.hhplus.concert.application.support.RedisClient
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.core.support.TokenManager
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.ScheduleService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReservationFacade(
    private val concertService: ConcertService,
    private val scheduleService: ScheduleService,
    private val paymentService: PaymentService,
    private val tokenManager: TokenManager,
    private val queue: RedisClient
) {

    private val maximumPassCount = 50

    fun reserve(userId: Long, scheduleId: Long): ReservationResult {
        val schedule = scheduleService.get(scheduleId)
        val now = LocalDateTime.now()

        if (schedule.sttReserveAt > now || now >= schedule.endReserveAt)
            throw BizException(BizError.Queue.CONNECTION_ERROR)

        queue.offer("WAIT::$scheduleId", userId)

        val token = tokenManager.createQueueToken(userId, schedule.concertId, scheduleId)

        return ReservationResult(
            token,
            "WAIT",
            scheduleId,
            queue.rank("WAIT::$scheduleId", userId)
        )
    }

    fun getStatus(token: String, queueId: Long, userId: Long, concertId: Long, scheduleId: Long): ReservationResult {
        val rank = queue.rank("WAIT::$scheduleId", userId)

        return if (rank > maximumPassCount) {
            ReservationResult(
                token,
                "WAIT",
                scheduleId,
                queue.rank("WAIT::$scheduleId", userId)
            )
        } else {
            val concert = concertService.get(concertId)
            val payment = paymentService.create(userId, concert.price)

            ReservationResult(
                tokenManager.createPaymentToken(userId, scheduleId, payment.id),
                "PASS",
                scheduleId,
                queue.rank("WAIT::$scheduleId", userId)
            ).also { queue.poll("WAIT::$scheduleId", userId) }
        }

    }
}
