package io.hhplus.concert.infrastructure.implement

import io.hhplus.concert.domain.concert.Concert
import io.hhplus.concert.domain.concert.ConcertRepository
import io.hhplus.concert.infrastructure.jpa.ConcertJpaRepository
import org.springframework.stereotype.Repository

@Repository
class ConcertRepositoryImpl(private val jpa: ConcertJpaRepository) : ConcertRepository {
    override fun save(concert: Concert): Concert =
        jpa.save(concert)

    override fun findById(id: Long): Concert? =
        jpa.findById(id).orElse(null)

    override fun findAll(): List<Concert> =
        jpa.findAll()
}