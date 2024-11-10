package io.hhplus.concert.presentation.schedule

import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.presentation.schedule.request.ScheduleRequest
import io.hhplus.concert.presentation.schedule.response.ScheduleResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/concerts/{concertId}/schedules")
class ScheduleController(private val service: ScheduleService) : IScheduleController {

    @PostMapping
    override fun create(@PathVariable concertId: Long, @RequestBody request: ScheduleRequest) =
        service.create(
            concertId,
            request.maximumReservableCount,
            request.sttAt,
            request.endAt,
            request.sttReserveAt,
            request.endReserveAt
        )
            .let { info -> ScheduleResponse(info) }
            .run {
                val uri = URI.create("concerts/${this.concertId}/schedules/${this.scheduleId}")
                ResponseEntity.created(uri).body(this)
            }


    @GetMapping("/{scheduleId}")
    override fun get(@PathVariable("scheduleId") scheduleId: Long) =
        service.get(scheduleId)
            .run { ScheduleResponse(this) }
            .run { ResponseEntity.ok(this) }

    @GetMapping
    override fun getAll(@PathVariable("concertId") concertId: Long) =
        service.getAll(concertId)
            .map { ScheduleResponse(it) }
            .run { ResponseEntity.ok(this) }

    @GetMapping("/{scheduleId}/reservable")
    override fun getReservable(@PathVariable("scheduleId") scheduleId: Long) =
        service.isReservable(scheduleId)
            .run { ResponseEntity.ok(this) }

    @GetMapping("/reservable")
    override fun getReservableSeatNumberList(@PathVariable("concertId") concertId: Long) =
        service.getReservableList(concertId)
            .run { ResponseEntity.ok(this) }

}