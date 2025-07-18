package com.hobom.hobominternal.adapter.inbound.rest.log

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Status Count Response")
data class HoBomLogRequestCountResponse(
    @Schema(description = "yyyy-MM-dd HH:mm:ss")
    val minute: String,

    @Schema(description = "Total request count")
    val totalRequests: Long,
)
