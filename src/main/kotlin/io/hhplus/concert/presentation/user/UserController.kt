package io.hhplus.concert.presentation.user

import io.hhplus.concert.domain.user.dto.PointHistoryResponse
import io.hhplus.concert.domain.user.dto.PointResponse
import io.hhplus.concert.domain.user.service.UserService
import io.hhplus.concert.presentation.user.dto.PointRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestBody as Request

@Tag(name = "1. 유저", description = "유저 및 유저 포인트 관련")
interface IUserController {
    @Operation(
        summary = "유저의 포인트 조회",
        description = "특정 유저의 포인트 정보를 반환합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    schema = Schema(implementation = PointResponse::class),
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "404", content = [Content(
                    schema = Schema(example = """{"error": "유저를 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun getPoint(@Parameter(description = "유저 ID") @PathVariable("id") userId: Long): Any

    @Operation(
        summary = "유저의 포인트 사용 이력 조회",
        description = "특정 유저의 포인트 사용 이력을 반환합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    array = ArraySchema(schema = Schema(implementation = PointHistoryResponse::class)),
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "404", content = [Content(
                    schema = Schema(example = """{"error": "유저를 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun getHistoryList(@Parameter(description = "유저 ID") @PathVariable("id") userId: Long): Any

    @Operation(
        summary = "유저의 포인트 충전",
        description = "특정 유저의 포인트를 충전합니다.",
        requestBody = RequestBody(
            description = "충전할 포인트 정보를 포함합니다.",
            required = true,
            content = [Content(schema = Schema(implementation = PointRequest::class), mediaType = "application/json")]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    schema = Schema(implementation = PointResponse::class),
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "404", content = [Content(
                    schema = Schema(example = """{"error": "유저를 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun chargePoint(
        @Parameter(description = "유저 ID") @PathVariable("id") userId: Long,
        @Request request: PointRequest
    ): Any
}


@RestController
@RequestMapping("/users")
class UserController(
    private val service: UserService
) : IUserController {

    @GetMapping("/{id}/points")
    override fun getPoint(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(service.getPoint(userId))

    @GetMapping("/{id}/points/histories")
    override fun getHistoryList(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(service.getHistoryList(userId))

    @PatchMapping("/{id}/points/charge")
    override fun chargePoint(@PathVariable("id") userId: Long, @Request(required = true) request: PointRequest) =
        ResponseEntity.ok().body(service.chargePoint(userId, request.point))
}