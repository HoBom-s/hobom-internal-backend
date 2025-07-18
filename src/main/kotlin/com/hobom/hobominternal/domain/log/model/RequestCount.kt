package com.hobom.hobominternal.domain.log.model

data class RequestCount(
    val minute: String,
    val totalRequests: Long,
)
