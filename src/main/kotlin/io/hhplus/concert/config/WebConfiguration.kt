package io.hhplus.concert.config

import io.hhplus.concert.config.filter.RequestTokenHeaderFilter
import io.hhplus.concert.config.interceptor.interceptor.TokenInterceptor
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    private val tokenHeaderFilter: RequestTokenHeaderFilter,
    private val tokenInterceptor: TokenInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenInterceptor)
            .addPathPatterns("/payment/**", "/queue/**")
    }

    @Bean
    fun addTokenFilter(): FilterRegistrationBean<Filter> {
        val bean = FilterRegistrationBean<Filter>()
        bean.filter = tokenHeaderFilter
        bean.addUrlPatterns("/queue/**", "/payment/**")
        return bean
    }


}