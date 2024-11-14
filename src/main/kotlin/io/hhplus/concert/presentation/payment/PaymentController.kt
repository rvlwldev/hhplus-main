package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.payment.PaymentFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pay")
class PaymentController(private val facade: PaymentFacade) : IPaymentController {

    @GetMapping("/seats")
    override fun getPayableSeatList(@RequestAttribute("scheduleId") scheduleId: Long) =
        facade.getPayableSeatList(scheduleId)
            .run { ResponseEntity.ok(this) }

    @PatchMapping("/seats/{seatId}")
    override fun ready(
        @RequestAttribute("userId") userId: Long,
        @RequestAttribute("scheduleId") scheduleId: Long,
        @PathVariable("seatId") seatId: Long
    ) = facade.ready(userId, scheduleId, seatId)
        .run { PaymentResponse(this) }
        .run { ResponseEntity.ok(this) }

    @PostMapping("/seats/{seatId}")
    override fun pay(
        @RequestAttribute("userId") userId: Long,
        @RequestAttribute("scheduleId") scheduleId: Long,
        @PathVariable("seatId") seatId: Long
    ) = facade.pay(userId, scheduleId, seatId)
        .run { PaymentResponse(this) }
        .run { ResponseEntity.ok(this) }

}