package io.hhplus.concert.core

import io.hhplus.concert.domain.seat.Seat
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory("127.0.0.1", 5555)
    }

    // RedisTemplate for List<Seat> 타입의 데이터
    @Bean
    fun listCacheRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, List<Seat>> {
        val template = RedisTemplate<String, List<Seat>>()
        template.connectionFactory = redisConnectionFactory
        return template
    }

    // RedisTemplate for Seat 타입의 데이터
    @Bean
    fun monoCacheRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Seat> {
        val template = RedisTemplate<String, Seat>()
        template.connectionFactory = redisConnectionFactory
        return template
    }

    // RedisTemplate for String 타입의 데이터
    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = redisConnectionFactory
        return template
    }
}