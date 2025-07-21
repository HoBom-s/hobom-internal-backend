package com.hobom.hobominternal.domain.log.model

import java.time.Instant

data class HoBomLog(
    val id: HoBomLogId? = null,
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
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
