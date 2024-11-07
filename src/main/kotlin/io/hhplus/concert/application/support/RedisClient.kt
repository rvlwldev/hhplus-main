package io.hhplus.concert.application.support

import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RedisClient(private val redis: RedisTemplate<String, String>) {

    fun offer(key: String, target: Long): Boolean {
        if (redis.opsForZSet().rank(key, "$target") != null)
            throw BizException(BizError.Queue.DUPLICATED)

        return redis.opsForZSet().add(key, "$target", Instant.now().epochSecond.toDouble())
            ?: throw BizException(BizError.Queue.CONNECTION_ERROR)
    }

    fun poll(key: String, target: Long): Long {
        return redis.opsForZSet().range(key, 0, 0)
            ?.takeIf { it.contains("$target") }
            ?.also { redis.opsForZSet().remove(key, "$target") }
            ?.first()
            ?.toLong()
            ?: throw BizException(BizError.Queue.NOT_FOUND)
    }

    fun rank(key: String, target: Long): Long =
        redis.opsForZSet().rank(key, "$target")
            ?: throw BizException(BizError.Queue.NOT_FOUND)
}
