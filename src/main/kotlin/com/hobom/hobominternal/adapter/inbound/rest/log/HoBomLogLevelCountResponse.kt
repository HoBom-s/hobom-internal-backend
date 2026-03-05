package com.hobom.hobominternal.adapter.inbound.rest.log

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Level Count Response")
data class HoBomLogLevelCountResponse(
    @Schema(description = "Log level (DEBUG, INFO, WARN, ERROR, FATAL)")
    val level: String,

    @Schema(description = "Count")
    val count: Long,
)
