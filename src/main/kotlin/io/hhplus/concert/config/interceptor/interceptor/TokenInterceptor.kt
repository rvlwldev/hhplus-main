package io.hhplus.concert.config.interceptor.interceptor

import io.hhplus.concert.config.support.TokenManager
import io.hhplus.concert.core.exception.BizException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Component
class TokenInterceptor(
    private val tokenManager: TokenManager
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any
    ): Boolean {
        val path = request.requestURI

        val token = request.getHeader("Authorization")
        val claims = if (path.startsWith("/payment")) tokenManager.validatePaymentToken(token)
        else if (path.startsWith("/queue")) tokenManager.validateQueueToken(token)
        else return true

        val currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val createdAt = claims["createdAt"] ?: throw BizException(HttpStatus.BAD_REQUEST, "토큰에 생성시간 정보가 없습니다.")
        val createdAtDateTime =
            LocalDateTime.ofEpochSecond(createdAt, 0, ZoneId.of("Asia/Seoul").rules.getOffset(LocalDateTime.now()))

        if (ChronoUnit.MINUTES.between(createdAtDateTime, currentTime) > 5) {
            throw BizException(HttpStatus.REQUEST_TIMEOUT, "유효시간이 초과되었습니다.")
        }

        return true
    }
}
