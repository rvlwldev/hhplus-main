package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.payment.PaymentFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pay")
class PaymentController(private val facade: PaymentFacade) {

    @GetMapping("/seats")
    fun getPayableSeatList(@RequestHeader("Authorization") token: String) = ResponseEntity.ok()
        .body(facade.getPayableSeatList(token)
            .map { PayableSeatResponse(it) })

    @PostMapping("/seats/{seatId}")
    fun pay(
        @RequestHeader("Authorization") token: String,
        @PathVariable("seatId") seatId: Long
    ) = ResponseEntity.ok()
        .body(
            facade.pay(token, seatId)
                .run { PaymentResponse(this) }
        )


}