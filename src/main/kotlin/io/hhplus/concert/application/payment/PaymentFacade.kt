package io.hhplus.concert.application.payment

import io.hhplus.concert.application.support.TokenManager
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
    private val tokenManager: TokenManager,
    private val seatService: SeatService,
    private val payService: PaymentService
) {

    @Transactional
    fun getPayableSeatList(token: String): List<PayableSeatResult> {
        val claims = tokenManager.validatePaymentToken(token)
        return seatService.getAllReservable(claims.getValue("scheduleId"))
            .map { PayableSeatResult(it) }
    }

    @Transactional
    fun pay(token: String, seatNumber: Long): PaymentResult {
        val claims = tokenManager.validatePaymentToken(token)
        val userId = claims.getValue("userId")
        val scheduleId = claims.getValue("scheduleId")

        return payService.pay(userId, scheduleId, seatNumber)
            .run { PaymentResult(this, scheduleId, seatNumber) }
    }

}