package io.hhplus.concert.presentation.queue

import io.hhplus.concert.application.reservation.ReservationCriteria
import io.hhplus.concert.application.reservation.ReservationFacade
import io.hhplus.concert.presentation.schedule.ScheduleReservationRequest
import io.hhplus.concert.presentation.schedule.ScheduleReservationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        val criteria = ReservationCriteria(request.userId, concertId, scheduleId)
        val response = facade.reserve(criteria)
            .run { ScheduleReservationResponse(this) }

        val uri = URI.create("/concerts/{concertId}/schedules/{scheduleId}/reserve")
        return ResponseEntity.created(uri)
            .body(response)
    }

    @GetMapping
    fun getStatus(@RequestHeader("Authorization") token: String) = facade.getStatus(token)
        .run { ScheduleReservationResponse(this) }

}