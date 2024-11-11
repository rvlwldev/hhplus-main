package io.hhplus.concert.presentation.payment

import org.springframework.http.ResponseEntity

interface IPaymentController {

    fun getPayableSeatList(scheduleId: Long): ResponseEntity<List<Long>>
    fun ready(userId: Long, scheduleId: Long, seatId: Long): ResponseEntity<PaymentResponse>
    fun pay(userId: Long, scheduleId: Long, seatId: Long): ResponseEntity<PaymentResponse>

}