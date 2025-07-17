package com.hobom.hobominternal.domain.log

import java.time.OffsetDateTime

data class RequestCount(
    val minute: String,
    val totalRequests: Long,
)
