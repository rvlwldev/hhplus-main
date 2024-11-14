package io.hhplus.concert.presentation.concert

import io.hhplus.concert.domain.concert.ConcertService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/concerts")
class ConcertController(private val service: ConcertService) : IConcertController {

    @PostMapping
    override fun create(@RequestBody request: ConcertRequest) =
        service.save(request.name, request.price)
            .run { ConcertResponse(this) }
            .run { ResponseEntity.created(URI.create("/concerts/${this.concertId}")).body(this) }

    @GetMapping
    override fun getAll() =
        service.getAll()
            .map { ConcertResponse(it) }
            .run { ResponseEntity.ok(this) }

    @GetMapping("/{concertId}")
    override fun get(@PathVariable concertId: Long) =
        service.get(concertId)
            .run { ConcertResponse(this) }
            .run { ResponseEntity.ok(this) }

}
