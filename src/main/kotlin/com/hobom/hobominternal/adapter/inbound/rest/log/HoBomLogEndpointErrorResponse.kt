package com.hobom.hobominternal.adapter.inbound.rest.log

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Endpoint Error Response")
data class HoBomLogEndpointErrorResponse(
    @Schema(description = "API path")
    val path: String,

    @Schema(description = "HTTP method")
    val httpMethod: String,

    @Schema(description = "Total request count")
    val totalCount: Long,

    @Schema(description = "Error count (status >= 400)")
    val errorCount: Long,

    @Schema(description = "Error rate (0.0 ~ 1.0)")
    val errorRate: Double,
)
