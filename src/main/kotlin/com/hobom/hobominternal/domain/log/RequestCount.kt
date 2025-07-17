package com.hobom.hobominternal.domain.log

data class RequestCount(
    val minute: String,
    val totalRequests: Long,
)
