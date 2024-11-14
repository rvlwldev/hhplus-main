package io.hhplus.concert.core.interceptor

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.core.support.TokenManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class PaymentInterceptor(private val tokenManager: TokenManager) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (request.requestURI.startsWith("/pay")) {
            try {
                val claims = tokenManager.validatePaymentToken(request.getHeader("Authorization"))
                request.setAttribute("userId", claims["userId"])
                request.setAttribute("scheduleId", claims["scheduleId"])
                return true
            } catch (e: BizException) {
                return false
            }
        }

        return true
    }
}