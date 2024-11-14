package io.hhplus.concert.core

import io.hhplus.concert.core.interceptor.QueueInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(private val queueInterceptor: QueueInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(queueInterceptor).addPathPatterns("/concerts/reserve")
    }

}