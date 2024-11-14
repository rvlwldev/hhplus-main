package io.hhplus.concert.infrastructure.flatform

import io.hhplus.concert.application.payment.DataPlatform
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DataPlatformImpl(private val rest: RestTemplate) : DataPlatform {
    override fun requestSeat(userId: Long, seatNumber: Long) {
        // TODO : rest 로 외부 API 호출

        println("회원 $userId 이 $seatNumber 예약 요청")
    }
}