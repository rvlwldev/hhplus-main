package io.hhplus.concert.presentation.queue

import io.hhplus.concert.application.reservation.ReservationFacadeV2
import io.hhplus.concert.presentation.schedule.request.ScheduleReservationRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleReservationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/concerts/reserve")
class QueueController(private val facade: ReservationFacadeV2) : IQueueController {

    @PostMapping
    override fun reserve(@RequestBody request: ScheduleReservationRequest) =
        facade.reserve(request.userId, request.scheduleId)
            .run { ScheduleReservationResponse(this) }
            .run { ResponseEntity.created(URI.create("/concerts/reserve")).body(this) }

    @GetMapping
    override fun getStatus(
        @RequestHeader("Authorization") token: String,
        @RequestAttribute("id") queueId: Long,
        @RequestAttribute("userId") userId: Long,
        @RequestAttribute("concertId") concertId: Long,
        @RequestAttribute("scheduleId") scheduleId: Long
    ) =
        facade.getStatus(token, queueId, userId, concertId, scheduleId)
            .run { ScheduleReservationResponse(this) }
            .run { ResponseEntity.ok(this) }

}
