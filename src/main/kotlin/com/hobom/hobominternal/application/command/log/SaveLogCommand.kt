package com.hobom.hobominternal.application.command.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HttpMethodType
import com.hobom.hobominternal.domain.log.ServiceType
import java.time.Instant

data class SaveLogCommand(
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
)

fun SaveLogCommand.toModel(): HoBomLog = HoBomLog(
    serviceType = serviceType,
    level = level,
    traceId = traceId,
    message = message,
    httpMethod = httpMethod,
    path = path,
    statusCode = statusCode,
    host = host,
    userId = userId,
    payload = payload,
    timestamp = Instant.now(),
)
