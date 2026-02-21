package com.hobom.hobominternal.adapter.inbound.rest.notion

import com.hobom.hobominternal.domain.notion.model.NotionBlockResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Tech Blog Contents Detail Response")
data class BlockChildrenResponse(
    @Schema(description = "Title")
    val title: String,
    @Schema(description = "Tags")
    val tags: List<String>,
    @Schema(description = "Contents")
    val contents: String,
) {
    companion object {
        fun from(result: NotionBlockResult): BlockChildrenResponse = BlockChildrenResponse(
            title = result.title,
            tags = result.tags,
            contents = result.contents,
        )
    }
}
