package io.hhplus.concert.config.filter

import io.hhplus.concert.config.support.TokenManager
import io.hhplus.concert.core.exception.BizException
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class RequestTokenHeaderFilter(
    private val tokenManager: TokenManager
) : HttpFilter() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        if (token == null || token.isEmpty())
            throw BizException(HttpStatus.UNAUTHORIZED, "토큰이 없습니다.")

        try {
            val path = request.requestURI

            if (path.startsWith("/payment"))
                tokenManager.validatePaymentToken(token)
            else if (path.startsWith("/queue"))
                tokenManager.validateQueueToken(token)
        } catch (e: BizException) {
            response.status = e.status.value()
            e.message?.let { response.writer.write(it) }
            return
        }

        chain.doFilter(request, response)
    }

    override fun init(filterConfig: FilterConfig?) {}

    override fun destroy() {}

}