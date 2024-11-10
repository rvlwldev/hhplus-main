package io.hhplus.concert.presentation.queue

import io.hhplus.concert.application.reservation.ReservationFacade
import io.hhplus.concert.presentation.schedule.request.ScheduleReservationRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleReservationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/concerts/reserve")
class QueueController(private val facade: ReservationFacade) : IQueueController {

    @PostMapping
    override fun reserve(@RequestBody request: ScheduleReservationRequest) =
        facade.reserve(request.userId, request.scheduleId)
            .run { ScheduleReservationResponse(this) }
            .run { ResponseEntity.created(URI.create("/concerts/reserve")).body(this) }

    // TODO : 필터랑 인터셉터에서 토큰값 분해하기
    @GetMapping
    override fun getStatus(@RequestHeader("Authorization") token: String) =
        facade.getStatus(token)
            .run { ScheduleReservationResponse(this) }
            .run { ResponseEntity.ok(this) }

}
