package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.model.HoBomLogLevel
import com.hobom.hobominternal.domain.log.model.HttpMethodType
import com.hobom.hobominternal.domain.log.model.ServiceType
import java.time.Instant

data class HoBomLogQueryResult(
    val id: HoBomLogId,
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
