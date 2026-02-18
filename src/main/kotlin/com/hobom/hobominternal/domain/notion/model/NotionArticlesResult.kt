package com.hobom.hobominternal.domain.notion.model

data class NotionArticlesResult(
    val articles: List<NotionArticle>,
    val nextCursor: String?,
    val hasMore: Boolean,
)

data class NotionArticle(
    val id: String,
    val title: String,
    val slug: String,
    val date: String?,
    val tags: List<String>,
    val emoji: String?,
)
