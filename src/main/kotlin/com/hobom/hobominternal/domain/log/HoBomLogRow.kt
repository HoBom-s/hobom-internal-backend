package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import java.time.Instant
import java.time.ZoneId

data class HoBomLogRow(
    val id: Long,
    val serviceType: String,
    val level: String,
    val traceId: String,
    val message: String,
    val httpMethod: String,
    val path: String?,
    val statusCode: Int,
    val host: String,
    val userId: String,
    val payload: String?,
    val timestamp: Instant,
) {
    fun toDomain(): HoBomLog = HoBomLog(
        id = id,
        serviceType = ServiceType.valueOf(serviceType),
        level = HoBomLogLevel.valueOf(level),
        traceId = traceId,
        message = message,
        httpMethod = HttpMethodType.valueOf(httpMethod),
        path = path,
        statusCode = statusCode,
        host = host,
        userId = userId,
        payload = payload,
        timestamp = timestamp.atZone(ZoneId.systemDefault()).toInstant(),
    )
}
