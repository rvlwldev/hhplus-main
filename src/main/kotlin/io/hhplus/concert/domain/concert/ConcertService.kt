package io.hhplus.concert.domain.concert

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.stereotype.Service

@Service
class ConcertService(private val repo: ConcertRepository) {

    fun save(name: String, price: Long) =
        repo.save(Concert(name = name, price = price))
            .run { ConcertInfo(this) }

    fun get(id: Long) = repo.findById(id)
        ?.run { ConcertInfo(this) }
        ?: throw BizException(BizError.Concert.NOT_FOUND)

    fun getAll() = repo.findAll()
        .map { ConcertInfo(it) }

}