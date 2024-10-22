package io.hhplus.concert.domain.concert

import org.springframework.stereotype.Service

@Service
class ConcertService(private val repo: ConcertRepository) {

    private final val NOT_FOUND_MESSAGE = "존재하지 않는 콘서트 정보입니다."

    fun save(name: String) = repo.save(name)
        .run { ConcertInfo(this) }

    fun get(id: Long) = repo.findById(id)
        ?: throw IllegalArgumentException(NOT_FOUND_MESSAGE)

    fun getAll() = repo.findAll()
        .map { ConcertInfo(it) }

}