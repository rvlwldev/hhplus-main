package io.hhplus.concert.application.support

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class RedisManager(private val redisTemplate: RedisTemplate<String, String>) {
    private val lockExpirationSeconds = 5L

    fun tryLock(lockKey: String): String? {
        val lockValue = UUID.randomUUID().toString()
        val isLockAcquired = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, lockValue, lockExpirationSeconds, TimeUnit.SECONDS) ?: false

        return if (isLockAcquired) lockValue else null
    }

    fun releaseLock(lockKey: String, lockValue: String) {
        val currentValue = redisTemplate.opsForValue().get(lockKey)
        if (currentValue == lockValue) {
            redisTemplate.delete(lockKey)
        }
    }

}