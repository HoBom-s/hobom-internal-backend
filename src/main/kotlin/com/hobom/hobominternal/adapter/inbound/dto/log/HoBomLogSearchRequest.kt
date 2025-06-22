package com.hobom.hobominternal.adapter.inbound.dto.log

import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "HoBom Log Condition For Filter")
data class HoBomLogSearchRequest(
    @Schema(description = "SearchType", example = "HOBOM_BACKEND")
    val serviceType: ServiceType? = null,

    @Schema(description = "HttpMethod", example = "POST")
    val httpMethod: HttpMethodType? = null,

    @Schema(description = "HttpStatusCode", example = "200")
    val statusCode: Int? = null,

    @Schema(description = "StartAt", example = "2024-01-01T00:00:00Z")
    val startedAt: Instant? = null,

    @Schema(description = "StartEnd", example = "2024-01-31T23:59:59Z")
    val endedAt: Instant? = null,
)

fun HoBomLogSearchRequest.toCriteria(): HoBomLogSearchCriteria = HoBomLogSearchCriteria(
    serviceType = this.serviceType,
    httpMethod = this.httpMethod,
    statusCode = this.statusCode,
    startedAt = this.startedAt,
    endedAt = this.endedAt,
)
