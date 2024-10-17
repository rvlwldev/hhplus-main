package io.hhplus.concert.presentation.payment

import io.hhplus.concert.application.PaymentFacade
import io.hhplus.concert.domain.payment.dto.PaymentHistoryResponse
import io.hhplus.concert.domain.payment.dto.PaymentResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "3. 결제", description = "특정 콘서트를 결제합니다.")
interface IPaymentController {
    @Operation(
        summary = "결제를 진행합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    schema = Schema(implementation = PaymentResponse::class),
                    mediaType = "application/json"
                )],
                description = "결제 결과를 반환합니다."
            ),
            ApiResponse(
                responseCode = "406",
                content = [Content(
                    schema = Schema(example = """{"error": "포인트가 부족합니다. 충전 후 다시 시도해주세요."}"""),
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "408",
                content = [Content(
                    schema = Schema(example = """{"error": "요청 시간이 초과되었습니다"}"""),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun pay(@Parameter(description = "결제 토큰") @RequestHeader("Authorization") paymentToken: String): Any

    @Operation(
        summary = "결제 이력을 조회합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = PaymentHistoryResponse::class))
                )]
            )
        ]
    )
    fun getHistoryList(@Parameter(description = "유저 ID") @PathVariable("id") userId: Long): Any

    @Operation(
        summary = "결제를 취소합니다.",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "결제 취소 완료"
            ),
            ApiResponse(
                responseCode = "404", content = [Content(
                    schema = Schema(example = """{"error": "결제 내역을 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )]
            ),
        ]
    )
    fun cancel(
        @PathVariable("id") userId: Long,
        @PathVariable("paymentId") paymentId: Long
    ): Any
}

data class PaymentRequest(
    val seatId: Long
)

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentFacade: PaymentFacade
) {

    @PostMapping
    fun pay(
        @RequestHeader("Authorization") payToken: String,
        @RequestBody request: PaymentRequest
    ) = ResponseEntity.ok().body(paymentFacade.pay(payToken, request.seatId))

    @GetMapping("/users/{id}/histories")
    fun getHistoryList(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(paymentFacade.getHistoryList(userId))
    
}


