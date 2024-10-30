package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.payment.PaymentFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pay")
class PaymentController(private val facade: PaymentFacade) {

    @GetMapping("/{scheduleId}/seats")
    fun getPayableSeatList(@PathVariable("scheduleId") scheduleId: Long) = ResponseEntity.ok()
        .body(facade.getPayableSeatList(scheduleId))

    @PostMapping("user/{userId}/{scheduleId}/seats/{seatId}")
    fun pay(
        @PathVariable("userId") userId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
        @PathVariable("seatId") seatId: Long
    ) = ResponseEntity.ok()
        .body(facade.pay(userId, scheduleId, seatId)
            .run { PaymentResponse(this) })

}