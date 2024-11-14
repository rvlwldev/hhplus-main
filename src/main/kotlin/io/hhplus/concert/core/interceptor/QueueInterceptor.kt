package io.hhplus.concert.core.interceptor

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.core.support.TokenManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class QueueInterceptor(private val tokenManager: TokenManager) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (request.requestURI == "/concerts/reserve" && request.method == "GET") {
            try {
                val claims = tokenManager.validateQueueToken(request.getHeader("Authorization"))
                request.setAttribute("id", claims["id"])
                request.setAttribute("userId", claims["userId"])
                request.setAttribute("concertId", claims["concertId"])
                request.setAttribute("scheduleId", claims["scheduleId"])
                request.setAttribute("status", claims["status"])
                return true
            } catch (e: BizException) {
                return false
            }
        }

        return true
    }
}