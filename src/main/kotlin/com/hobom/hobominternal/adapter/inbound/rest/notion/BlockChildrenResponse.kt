package com.hobom.hobominternal.adapter.inbound.rest.notion

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Tech Blog Contents Detail Response")
data class BlockChildrenResponse(
    @Schema(description = "Contents")
    val contents: String,
) {
    companion object {
        fun from(contents: String): BlockChildrenResponse = BlockChildrenResponse(
            contents = contents,
        )
    }
}
