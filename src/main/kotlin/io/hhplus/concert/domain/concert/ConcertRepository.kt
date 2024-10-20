package io.hhplus.concert.domain.concert

interface ConcertRepository {
    fun save(concert: Concert): Concert
    fun save(name: String): Concert

    fun findById(id: Long): Concert?
    fun findAll(): List<Concert>
}