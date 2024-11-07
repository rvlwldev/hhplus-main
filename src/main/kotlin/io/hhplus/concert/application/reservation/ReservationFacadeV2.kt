package io.hhplus.concert.application.reservation

import io.hhplus.concert.application.reservation.dto.ReservationResultV2
import io.hhplus.concert.application.support.RedisClient
import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.ScheduleService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReservationFacadeV2(
    private val concertService: ConcertService,
    private val scheduleService: ScheduleService,
    private val paymentService: PaymentService,
    private val tokenManager: TokenManager,
    private val queue: RedisClient
) {

    private val maximumPassCount = 50

    fun reserve(userId: Long, scheduleId: Long): ReservationResultV2 {
        val schedule = scheduleService.get(scheduleId)
        val now = LocalDateTime.now()

        if (schedule.sttReserveAt > now || now >= schedule.endReserveAt)
            throw BizException(BizError.Queue.CONNECTION_ERROR)

        queue.offer("WAIT::$scheduleId", userId)

        val token = tokenManager.createQueueToken(userId, scheduleId)

        return ReservationResultV2(
            token,
            "WAIT",
            scheduleId,
            queue.rank("WAIT::$scheduleId", userId)
        )
    }

    fun getStatus(token: String): ReservationResultV2 {
        val claims = tokenManager.validateQueueToken(token)

        val userId = claims.getValue("userId") as Long
        val concertId = claims.getValue("concertId") as Long
        val scheduleId = claims.getValue("scheduleId") as Long
        val rank = queue.rank("WAIT::$scheduleId", userId)

        return if (rank > maximumPassCount) {
            ReservationResultV2(
                token,
                "WAIT",
                scheduleId,
                queue.rank("WAIT::$scheduleId", userId)
            )
        } else {
            val concert = concertService.get(concertId)
            val payment = paymentService.create(userId, concert.price)

            ReservationResultV2(
                tokenManager.createPaymentToken(userId, scheduleId, payment.id),
                "PASS",
                scheduleId,
                queue.rank("WAIT::$scheduleId", userId)
            ).also { queue.poll("WAIT::$scheduleId", userId) }
        }

    }
}
