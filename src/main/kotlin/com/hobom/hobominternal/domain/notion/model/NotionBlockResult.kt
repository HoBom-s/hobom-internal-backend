package com.hobom.hobominternal.domain.notion.model

data class NotionBlockResult(
    val title: String,
    val tags: List<String>,
    val contents: String,
)
