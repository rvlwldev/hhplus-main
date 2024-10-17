package io.hhplus.concert.presentation.payment

import io.hhplus.concert.presentation.payment.response.PaymentHistoryResponse
import io.hhplus.concert.presentation.payment.response.PaymentResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payments")
class PaymentController {

    @PostMapping
    fun pay(@RequestHeader("Authorization") payToken: String) = ResponseEntity.ok()
        .body(
            PaymentResponse(
                1,
                1,
                70000,
                "2024-10-31 14:30:00",
                "2024-10-31 15:30:00"
            )
        )

    @GetMapping("/users/{id}/histories")
    fun getHistoryList(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(
            listOf(
                PaymentHistoryResponse(
                    1,
                    1,
                    1,
                    "CHARGE",
                    "2024-10-10 14:30:00",
                    50000
                ),
                PaymentHistoryResponse(
                    2,
                    2,
                    1,
                    "USE",
                    "2024-11-10 14:30:00",
                    25000
                ),
            )
        )

    @DeleteMapping("/users/{id}/histories/{paymentId}")
    fun cancel(
        @PathVariable("id") userId: Long,
        @PathVariable("paymentId") paymentId: Long
    ) = ResponseEntity.noContent()

}