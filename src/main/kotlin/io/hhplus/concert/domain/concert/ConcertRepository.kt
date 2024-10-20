package io.hhplus.concert.domain.concert

interface ConcertRepository {
    fun save(concert: Concert): Concert
    fun save(name: String): Concert

    fun findConcertById(id: Long): Concert?
    fun findAllConcert(): List<Concert>
}