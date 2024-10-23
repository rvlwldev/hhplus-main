package io.hhplus.concert.domain.concert

import org.springframework.stereotype.Service

@Service
class ConcertService(private val repo: ConcertRepository) {

    fun save(name: String, price: Long) = repo.save(Concert(name = name, price = price))
        .run { ConcertInfo(this) }

    fun get(id: Long) = repo.findById(id)
        ?.run { ConcertInfo(this) }
        ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

    fun getAll() = repo.findAll()
        .map { ConcertInfo(it) }

    companion object {
        private const val NOT_FOUND_MESSAGE = "존재하지 않는 콘서트 정보입니다."
    }

}