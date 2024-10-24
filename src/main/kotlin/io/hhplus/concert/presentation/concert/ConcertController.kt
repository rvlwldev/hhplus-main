package io.hhplus.concert.presentation.concert

import io.hhplus.concert.domain.concert.ConcertService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/concerts")
class ConcertController(private val service: ConcertService) {

    @PostMapping
    fun create(@RequestBody request: ConcertRequest) = service.save(request.name, request.price)
        .run { ConcertResponse(this) }

    @GetMapping
    fun getAll() = service.getAll()
        .map { ConcertResponse(it) }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long) = service.get(id)
        .run { ConcertResponse(this) }

}
