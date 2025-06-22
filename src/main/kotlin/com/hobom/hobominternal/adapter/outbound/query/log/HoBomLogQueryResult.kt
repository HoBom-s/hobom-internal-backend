package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLog

data class HoBomLogQueryResult(
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
    val timestamp: String,
)

fun HoBomLog.toQueryResult(): HoBomLogQueryResult =
    HoBomLogQueryResult(
        id = id,
        serviceType = serviceType.name,
        level = level.name,
        traceId = traceId,
        message = message,
        httpMethod = httpMethod.name,
        path = path,
        statusCode = statusCode,
        host = host,
        userId = userId,
        payload = payload,
        timestamp = timestamp.toString(), // or use DateTimeFormatter.ISO_INSTANT
    )
