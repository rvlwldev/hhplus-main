package io.hhplus.concert.application.payment

import io.hhplus.concert.application.payment.result.PaymentResult
import io.hhplus.concert.application.support.RedisManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.payment.PaymentStatus
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.seat.SeatService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentFacade(
    private val redisManager: RedisManager,
    private val concertService: ConcertService,
    private val scheduleService: ScheduleService,
    private val seatService: SeatService,
    private val paymentService: PaymentService
) {

    @Transactional
    fun getPayableSeatList(scheduleId: Long): List<Long> =
        scheduleService.getReservableList(scheduleId)

    @Transactional
    fun ready(userId: Long, scheduleId: Long, seatNumber: Long): PaymentResult {
        val lockKey = "reservation:lock:$scheduleId:$seatNumber"
        var lockVal: String? = null

        val maxRetries = 10
        val retryDelay = 1000L * 3L
        var retryCount = 0

        while (lockVal == null && retryCount < maxRetries) {
            lockVal = redisManager.tryLock(lockKey)

            if (lockVal == null) {
                val seat = seatService.get(scheduleId, seatNumber)
                if (seat != null && seat.status !== "EMPTY")
                    throw BizException(BizError.Seat.ALREADY_RESERVED)

                retryCount++
                Thread.sleep(retryDelay)
            }
        }

        if (lockVal == null)
            throw BizException(BizError.Payment.ALREADY_IN_PROGRESS)

        try {
            if (paymentService.getLatestByUserId(userId).status == PaymentStatus.PAID.name)
                throw BizException(BizError.Payment.DUPLICATED)

            val seat = seatService.getOrCreate(userId, seatNumber)
            seatService.readyToReserve(seat.id, userId)

            val schedule = scheduleService.get(scheduleId)
            val concert = concertService.get(schedule.id)

            return paymentService.create(userId, concert.price)
                .run { PaymentResult(this, scheduleId, seatNumber) }
        } finally {
            redisManager.releaseLock(lockKey, lockVal)
        }

    }

    @Transactional
    fun pay(userId: Long, scheduleId: Long, seatNumber: Long): PaymentResult {
        val seat = seatService.getOrCreate(userId, seatNumber)
        seatService.confirm(seat.id)

        return paymentService.pay(userId)
            .run { PaymentResult(this, scheduleId, seatNumber) }
    }

}
