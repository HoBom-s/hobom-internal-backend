package com.hobom.hobominternal.adapter.inbound.dto.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Search Response")
data class HoBomLogSearchResponse(
    @Schema(description = "Log UUID")
    val id: Long,

    @Schema(description = "SearchType")
    val serviceType: String,

    @Schema(description = "LogLevel", example = "INFO")
    val level: String,

    @Schema(description = "TraceId")
    val traceId: String,

    @Schema(description = "Message")
    val message: String,

    @Schema(description = "HttpMethod")
    val httpMethod: String,

    @Schema(description = "HttpEndPoint")
    val path: String?,

    @Schema(description = "HttpStatusCode")
    val statusCode: Int,

    @Schema(description = "Host", example = "hobom-system(React App)")
    val host: String,

    @Schema(description = "UserId")
    val userId: String,

    @Schema(description = "Payload")
    val payload: String?,

    @Schema(description = "Timestamp")
    val timestamp: String,
) {
    companion object {
        fun from(query: HoBomLogQueryResult): HoBomLogSearchResponse = HoBomLogSearchResponse(
            id = query.id,
            serviceType = query.serviceType,
            level = query.level,
            traceId = query.traceId,
            message = query.message,
            httpMethod = query.httpMethod,
            path = query.path,
            statusCode = query.statusCode,
            host = query.host,
            userId = query.userId,
            payload = query.payload,
            timestamp = query.timestamp,
        )
    }
}
