package io.hhplus.concert.presentation.concert

import org.springframework.http.ResponseEntity

interface IConcertController {

    fun create(request: ConcertRequest): ResponseEntity<ConcertResponse>
    fun getAll(): ResponseEntity<List<ConcertResponse>>
    fun get(concertId: Long): ResponseEntity<ConcertResponse>

}