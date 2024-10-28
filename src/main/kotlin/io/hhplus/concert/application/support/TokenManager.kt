package io.hhplus.concert.application.support

import io.hhplus.concert.core.exception.BizException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class TokenManager {

    enum class Type {
        RESERVATION, PAYMENT
    }

    private val queueTokenSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    private val paymentTokenSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val paymentTokenExpiration: Long = 20 * 60 * 1000L // 20분

    private fun createToken(userId: Long, secret: Key, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secret)
            .compact()
    }

    fun createQueueToken(id: Long, userId: Long, scheduleId: Long, status: String): String {
        val claims = HashMap<String, Any>()
        claims["id"] = id
        claims["userId"] = userId
        claims["scheduleId"] = scheduleId
        claims["status"] = status
        claims["createdAt"] = LocalDateTime.now().toString()

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .signWith(queueTokenSecret)
            .compact()
    }

    fun createPaymentToken(userId: Long, scheduleId: Long, paymentId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + paymentTokenExpiration)
        val claims = HashMap<String, Long>()
        claims["userId"] = userId
        claims["scheduleId"] = scheduleId
        claims["paymentId"] = paymentId
        claims["createdAt"] = LocalDateTime.now().toEpochSecond(
            ZoneId.of("Asia/Seoul").rules.getOffset(LocalDateTime.now())
        )

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(paymentTokenSecret)
            .compact()
    }

    fun validateQueueToken(token: String): Map<String, Any> {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(queueTokenSecret)
                .build()
                .parseClaimsJws(token)
                .body

            val resultMap = HashMap<String, Any>()
            resultMap["id"] = claims["id"].toString().toLong()
            resultMap["userId"] = claims["userId"].toString().toLong()
            resultMap["scheduleId"] = claims["scheduleId"].toString().toLong()
            resultMap["status"] = claims["status"].toString()
            resultMap["createdAt"] = LocalDateTime.parse(claims["createdAt"].toString())

            return resultMap
        } catch (e: ExpiredJwtException) {
            throw BizException(HttpStatus.REQUEST_TIMEOUT, "인증시간이 초과되었습니다.")
        } catch (e: Exception) {
            throw BizException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
        }
    }

    fun validatePaymentToken(token: String): Map<String, Long> {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(paymentTokenSecret)
                .build()
                .parseClaimsJws(token)
                .body

            val resultMap = HashMap<String, Long>()
            resultMap["userId"] = claims["userId"].toString().toLong()
            resultMap["scheduleId"] = claims["scheduleId"].toString().toLong()

            return resultMap
        } catch (e: ExpiredJwtException) {
            throw BizException(HttpStatus.REQUEST_TIMEOUT, "인증시간이 초과되었습니다.")
        } catch (e: Exception) {
            throw BizException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
        }

    }

}
