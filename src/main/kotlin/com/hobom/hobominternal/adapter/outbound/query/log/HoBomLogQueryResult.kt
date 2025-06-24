package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HttpMethodType
import com.hobom.hobominternal.domain.log.ServiceType
import java.time.Instant

data class HoBomLogQueryResult(
    val id: Long,
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
) {
    companion object {
        fun from(hoBomLog: HoBomLog): HoBomLogQueryResult = HoBomLogQueryResult(
            id = hoBomLog.id!!,
            serviceType = hoBomLog.serviceType,
            level = hoBomLog.level,
            traceId = hoBomLog.traceId,
            message = hoBomLog.message,
            httpMethod = hoBomLog.httpMethod,
            path = hoBomLog.path,
            statusCode = hoBomLog.statusCode,
            host = hoBomLog.host,
            userId = hoBomLog.userId,
            payload = hoBomLog.payload,
            timestamp = hoBomLog.timestamp,
        )
    }
}
