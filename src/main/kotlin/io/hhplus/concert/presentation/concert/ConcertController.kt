package io.hhplus.concert.presentation.concert

import io.hhplus.concert.presentation.concert.response.ConcertResponse
import io.hhplus.concert.presentation.concert.response.ConcertScheduleResponse
import io.hhplus.concert.presentation.concert.response.TokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/concerts")
class ConcertController {

    @GetMapping
    fun getList() = ResponseEntity.ok()
        .body(
            listOf(
                ConcertResponse(1, "허재의 카드마술쇼", 70000L),
                ConcertResponse(2, "하헌우의 라이브 코딩쇼", 70000L),
            )
        )

    @GetMapping("/{concertId}/schedules")
    fun getScheduleList(@PathVariable("concertId") concertId: Long) = ResponseEntity.ok()
        .body(
            listOf(
                ConcertScheduleResponse(
                    1,
                    "2024-10-31 14:30:00",
                    "2024-10-31 15:30:00",
                    50,
                    1
                ),
                ConcertScheduleResponse(
                    2,
                    "2024-11-31 14:30:00",
                    "2024-11-31 15:30:00",
                    50,
                    0
                )
            )
        )

    @GetMapping("/{concertId}/schedules/{scheduleId}/reserves")
    fun getQueueResponse(
        @RequestHeader("Authorization", required = true) queueToken: String,
        @PathVariable("concertId") concertId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
    ) = ResponseEntity.ok().body(TokenResponse("ey..."))

    @PutMapping("/{concertId}/schedules/{scheduleId}/reserves")
    fun reserve(
        @PathVariable("concertId") concertId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
    ) = ResponseEntity
        .created(URI.create("/concerts/$concertId/schedules/$scheduleId/reserves"))
        .body(TokenResponse("ey..."))
}