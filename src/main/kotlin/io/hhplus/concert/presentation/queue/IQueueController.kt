package io.hhplus.concert.presentation.queue

import io.hhplus.concert.presentation.schedule.request.ScheduleReservationRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleReservationResponse
import org.springframework.http.ResponseEntity

interface IQueueController {

    fun reserve(request: ScheduleReservationRequest): ResponseEntity<ScheduleReservationResponse>
    fun getStatus(token: String): ResponseEntity<ScheduleReservationResponse>

}