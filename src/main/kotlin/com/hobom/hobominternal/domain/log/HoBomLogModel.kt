package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import java.time.Instant

data class HoBomLogModel(
    val serviceType: ServiceType,
    val level: HoBomLogLevel,
    val traceId: String,
    val message: String,
    val httpMethod: HttpMethodType,
    val path: String?,
    val statusCode: Int,
    val host: String,
    val userId: String,
    val payload: Map<String, Any>?,
    val timestamp: Instant,
)
