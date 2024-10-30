package io.hhplus.concert.presentation.queue

import io.hhplus.concert.application.reservation.ReservationFacade
import io.hhplus.concert.presentation.schedule.request.ScheduleReservationRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleReservationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/concerts/{concertId}/schedules/{scheduleId}/reserve")
class QueueController(private val facade: ReservationFacade) {

    @PostMapping
    fun reserve(
        @PathVariable("concertId") concertId: Long,
        @PathVariable("scheduleId") scheduleId: Long,
        @RequestBody request: ScheduleReservationRequest
    ): ResponseEntity<Any> {
        val response = facade.reserve(request.userId, scheduleId)
            .run { ScheduleReservationResponse(this) }

        val uri = URI.create("/concerts/{concertId}/schedules/{scheduleId}/reserve")
        return ResponseEntity.created(uri)
            .body(response)
    }

    @GetMapping
    fun getStatus(@RequestHeader("Authorization") token: String) = facade.getStatus(token)
        .run { ScheduleReservationResponse(this) }

}