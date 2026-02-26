package com.hobom.hobominternal.config

import feign.RequestInterceptor
import feign.RequestTemplate

class HoBomBackendFeignConfig : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        requestTemplate.header("Content-Type", "application/json")
    }
}
