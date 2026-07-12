package com.hobom.hobominternal.adapter.inbound.rest.log

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Service Type Count Response")
data class HoBomLogServiceCountResponse(
    @Schema(description = "Service type")
    val serviceType: String,

    @Schema(description = "Request count")
    val count: Long,
)
