package io.hhplus.concert.presentation.schedule

import io.hhplus.concert.domain.schedule.ScheduleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/concerts/{concertId}/schedules")
class ScheduleController(private val service: ScheduleService) {

    @GetMapping
    fun getAll(
        @PathVariable("concertId") concertId: Long,
        @PathVariable("scheduleId") scheduleId: Long
    ) = ResponseEntity.ok()
        .body(service.getAll(concertId)
            .map { ScheduleResponse(it) })

    @GetMapping("/{scheduleId}")
    fun getOne(@PathVariable("scheduleId") scheduleId: Long) = ResponseEntity.ok()
        .body(service.get(scheduleId)
            .run { ScheduleResponse(this) })

    @GetMapping("/reservable")
    fun getReservableList(@PathVariable("concertId") concertId: Long) = ResponseEntity.ok()
        .body(service.getReservableList(concertId)
            .map { ScheduleResponse(it) })

}