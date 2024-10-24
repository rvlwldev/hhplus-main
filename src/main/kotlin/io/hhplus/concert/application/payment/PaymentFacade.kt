package io.hhplus.concert.application.payment

import io.hhplus.concert.application.payment.result.PayableSeatResult
import io.hhplus.concert.application.payment.result.PaymentResult
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.seat.SeatService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * TODO : 241027 Pay 파사드 구현
 *
 * 1. 생성시간을 기준으로 Pay 정보 스케줄러로 삭제
 * 2. Pay 대기상태구현 (생성 시간)
 * 3. Pay 성공 시 시간 비교
 *
 */

@Service
class PaymentFacade(
    private val seatService: SeatService,
    private val payService: PaymentService
) {

    @Transactional
    fun getPayableSeatList(scheduleId: Long): List<PayableSeatResult> {
        return seatService.getAllReservable(scheduleId)
            .map { PayableSeatResult(it) }
    }

    @Transactional
    fun pay(userId: Long, scheduleId: Long, seatNumber: Long): PaymentResult {
        return payService.pay(userId, scheduleId, seatNumber)
            .run { PaymentResult(this, scheduleId, seatNumber) }
    }
}
