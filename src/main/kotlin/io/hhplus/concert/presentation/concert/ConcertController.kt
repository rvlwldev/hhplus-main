package io.hhplus.concert.presentation.concert

import io.hhplus.concert.application.ConcertReservationFacade
import io.hhplus.concert.domain.concert.dto.ConcertResponse
import io.hhplus.concert.domain.concert.dto.ConcertScheduleResponse
import io.hhplus.concert.domain.concert.ConcertService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Tag(name = "2. 콘서트", description = "콘서트 관련 기능 관리")
interface IConcertController {
    @Operation(
        summary = "콘서트 목록 조회",
        description = "모든 콘서트 목록을 조회합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    array = ArraySchema(schema = Schema(implementation = ConcertResponse::class)),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun getList(): Any

    @Operation(
        summary = "콘서트 일정 조회",
        description = "특정 콘서트의 일정 목록을 조회합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    array = ArraySchema(schema = Schema(implementation = ConcertScheduleResponse::class)),
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "404",
                content = [Content(
                    schema = Schema(example = """{"error": "콘서트를 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )]
            )
        ]
    )
    fun getScheduleList(@Parameter(description = "콘서트 ID") @PathVariable("concertId") concertId: Long): Any

    @Operation(
        summary = "대기열 상태 조회",
        description = "현재 대기열의 상태를 조회합니다. 대기열 종료 시 결재토큰이 반환됩니다.",
        responses =
        [

        ]
    )
    fun getQueueResponse(
        @Parameter(description = "발급받은 대기열 토큰") @RequestHeader("Authorization", required = true) queueToken: String,
        @Parameter(description = "콘서트 ID") @PathVariable("concertId") concertId: Long,
        @Parameter(description = "콘서트 일정 ID") @PathVariable("scheduleId") scheduleId: Long,
    ): Any

    @Operation(
        summary = "콘서트 예약",
        description = "특정 콘서트의 특정 일정을 예약 신청합니다.",
        responses = [
            ApiResponse(
                responseCode = "201",
                content = [Content(
                    mediaType = "application/json"
                )],
                description = "예약 성공 시 예약 상세 정보 반환"
            ),
            ApiResponse(
                responseCode = "404",
                content = [Content(
                    schema = Schema(example = """{"error": "콘서트 또는 해당 일정을 찾을 수 없습니다."}"""),
                    mediaType = "application/json"
                )],
                description = "콘서트 ID, 또는 일정 ID가 존재하지 않을 경우 입니다."
            ),
            ApiResponse(
                responseCode = "406",
                content = [Content(
                    schema = Schema(example = """{"error": "매진된 일정입니다."}"""),
                    mediaType = "application/json"
                )],
            )]
    )
    fun reserve(
        @Parameter(description = "콘서트 ID") @PathVariable("concertId") concertId: Long,
        @Parameter(description = "콘서트 일정 ID") @PathVariable("scheduleId") scheduleId: Long,
    ): Any
}


@RestController
@RequestMapping("/concerts")
class ConcertController(
    private val service: ConcertService,
    private val facade: ConcertReservationFacade
) {

    @GetMapping
    fun getList() = ResponseEntity.ok()
        .body(service.getAll())

    @GetMapping("/{concertId}/schedules")
    fun getScheduleList(@PathVariable("concertId") concertId: Long) = ResponseEntity.ok()
        .body(service.getScheduleList(concertId))

    @GetMapping("/schedules/{scheduleId}")
    fun getSchedule(@PathVariable("scheduleId") scheduleId: Long) = ResponseEntity.ok()
        .body(service.getSchedule(scheduleId))

    @GetMapping("/schedules/{scheduleId}/seats")
    fun getSeatList(@PathVariable("scheduleId") scheduleId: Long) = ResponseEntity.ok()

    @GetMapping("/schedules/{scheduleId}/reserves/wait")
    fun getQueueResponse(
        @RequestHeader("Authorization", required = true) queueToken: String,
        @PathVariable("scheduleId") scheduleId: Long,
    ) = ResponseEntity.ok().body(facade.waitQueue(queueToken, scheduleId))

    @PutMapping("/schedules/{scheduleId}/users/{userId}/reserves")
    fun reserve(
        @PathVariable("userId") userId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
    ) = ResponseEntity.ok().body(facade.reserve(userId, scheduleId))
}