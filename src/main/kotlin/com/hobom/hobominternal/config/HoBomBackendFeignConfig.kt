package com.hobom.hobominternal.config

import feign.Request
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit

class HoBomBackendFeignConfig : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        requestTemplate.header("Content-Type", "application/json")
    }

    @Bean
    fun hobomBackendFeignOptions(): Request.Options = Request.Options(
        5,
        TimeUnit.SECONDS,
        10,
        TimeUnit.SECONDS,
        true,
    )
}
