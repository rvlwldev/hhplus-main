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
class PaymentController(private val facade: PaymentFacade) : IPaymentController {

    @GetMapping("/{scheduleId}/seats")
    override fun getPayableSeatList(@PathVariable("scheduleId") scheduleId: Long) =
        facade.getPayableSeatList(scheduleId)
            .run { ResponseEntity.ok(this) }

    @PostMapping("user/{userId}/{scheduleId}/seats/{seatId}")
    override fun pay(
        @PathVariable("userId") userId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
        @PathVariable("seatId") seatId: Long
    ) = facade.pay(userId, scheduleId, seatId)
        .run { PaymentResponse(this) }
        .run { ResponseEntity.ok(this) }

}