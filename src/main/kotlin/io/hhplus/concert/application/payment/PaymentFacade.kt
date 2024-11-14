package io.hhplus.concert.application.payment

import io.hhplus.concert.application.payment.result.PaymentResult
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.seat.SeatService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentFacade(
    private val concertService: ConcertService,
    private val scheduleService: ScheduleService,
    private val seatService: SeatService,
    private val paymentService: PaymentService,
) {
    fun getPayableSeatList(scheduleId: Long): List<Long> =
        scheduleService.getReservableList(scheduleId)

    fun ready(userId: Long, scheduleId: Long, seatNumber: Long): PaymentResult {
        val seat = seatService.getOrCreate(userId, seatNumber)
        seatService.readyToReserve(seat.id, userId)

        val schedule = scheduleService.get(scheduleId)
        val concert = concertService.get(schedule.id)

        return paymentService.create(userId, concert.price)
            .run { PaymentResult(this, scheduleId, seatNumber) }
    }

    @Transactional
    fun pay(userId: Long, scheduleId: Long, seatNumber: Long): PaymentResult {
        val seat = seatService.getOrCreate(userId, seatNumber)
        seatService.confirm(seat.id)

        return paymentService.pay(userId)
            .run { PaymentResult(this, scheduleId, seatNumber) }
    }
}