package com.hobom.hobominternal.shared.page

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PageRequest(
    @field:Min(0)
    @Schema(description = "PageNumber")
    val page: Int,

    @field:Min(1)
    @field:Max(100)
    @Schema(description = "PageSize")
    val size: Int,
)
