package io.hhplus.concert.application

import io.hhplus.concert.application.component.TokenProvider
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.payment.dto.PaymentResponse
import io.hhplus.concert.domain.payment.entity.Payment
import io.hhplus.concert.domain.queue.QueueService
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PaymentFacade(
    private val tokenProvider: TokenProvider,
    private val paymentService: PaymentService,
    private val queueService: QueueService
) {

    fun pay(payToken: String, seatId: Long): PaymentResponse {
        val claims = try {
            tokenProvider.validatePaymentToken(payToken)
        } catch (e: ExpiredJwtException) {
            val claims = e.claims
            val userId = claims["userId"].toString().toLong()
            val scheduleId = claims["scheduleId"].toString().toLong()
            queueService.deleteByUserIdAndScheduleId(userId, scheduleId)

            throw BizException(HttpStatus.REQUEST_TIMEOUT, "인증시간이 초과되었습니다.")
        } catch (e: Exception) {
            throw BizException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
        }

        return paymentService.pay(claims["userId"]!!, claims["scheduleId"]!!, seatId)
    }

    fun getHistoryList(userId: Long): Result<List<Payment>> {
        val historyList = paymentService.getList(userId)
        return Result.success(historyList)
    }

}