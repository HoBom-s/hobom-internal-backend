package com.hobom.hobominternal.adapter.inbound.rest.log

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Log Status Count Response")
data class HoBomLogStatusCountResponse(
    @Schema(description = "Status code")
    val statusCode: Int,

    @Schema(description = "Status code count")
    val count: Int,
)
