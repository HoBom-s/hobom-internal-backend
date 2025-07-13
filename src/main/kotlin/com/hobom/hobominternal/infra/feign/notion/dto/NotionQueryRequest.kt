package com.hobom.hobominternal.infra.feign.notion.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NotionQueryRequest(
    val page_size: Int,
    val start_cursor: String?,
    val filter: Map<String, Any>? = null,
    val sorts: List<Map<String, Any>>? = null,
)
