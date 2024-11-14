package io.hhplus.concert.presentation.schedule

import io.hhplus.concert.presentation.schedule.request.ScheduleRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleResponse
import org.springframework.http.ResponseEntity

interface IScheduleController {

    fun create(concertId: Long, request: ScheduleRequest): ResponseEntity<ScheduleResponse>
    fun get(scheduleId: Long): ResponseEntity<ScheduleResponse>
    fun getAll(concertId: Long): ResponseEntity<List<ScheduleResponse>>
    fun getReservable(scheduleId: Long): ResponseEntity<Boolean>
    fun getReservableSeatNumberList(concertId: Long): ResponseEntity<List<Long>>

}