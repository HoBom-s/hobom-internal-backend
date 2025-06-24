package com.hobom.hobominternal.shared.page

import io.swagger.v3.oas.annotations.media.Schema

data class PageRequest(
    @Schema(description = "PageNumber")
    val page: Int,

    @Schema(description = "PageSize")
    val size: Int,
)
