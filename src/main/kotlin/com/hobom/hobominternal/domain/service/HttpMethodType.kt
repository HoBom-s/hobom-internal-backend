package com.hobom.hobominternal.domain.service

import com.hobom.hobominternal.config.DescribableEnum

enum class HttpMethodType(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    GET("GET", "Http GET Method"),
    POST("POST", "Http POST Method"),
    PATCH("PATCH", "Http PATCH Method"),
    PUT("PUT", "Http PUT Method"),
    DELETE("DELETE", "Http DELETE Method"),
}
