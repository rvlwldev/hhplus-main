package io.hhplus.concert.core.support

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

    private val queueTokenSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    private val paymentTokenSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val paymentTokenExpiration: Long = 20 * 60 * 1000L // 20분

    fun createQueueToken(userId: Long, concertId: Long, scheduleId: Long): String {
        val claims = HashMap<String, Any>()
        claims["userId"] = userId
        claims["concertId"] = concertId
        claims["scheduleId"] = scheduleId
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
        val offset = ZoneId.of("Asia/Seoul").rules.getOffset(LocalDateTime.now())
        val expiryDate = Date(now.time + paymentTokenExpiration)

        val claims = HashMap<String, Long>()
        claims["userId"] = userId
        claims["scheduleId"] = scheduleId
        claims["paymentId"] = paymentId
        claims["createdAt"] = LocalDateTime.now().toEpochSecond(offset)

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
            resultMap["userId"] = claims["userId"].toString().toLong()
            resultMap["concertId"] = claims["concertId"].toString().toLong()
            resultMap["scheduleId"] = claims["scheduleId"].toString().toLong()
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
