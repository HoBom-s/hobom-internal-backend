package com.hobom.hobominternal.adapter.inbound.dto.notion

import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "HoBom Tech Blog Article Response")
data class ArticleResponse(
    @Schema(description = "Article's Summary")
    val articles: List<ArticleSummary>,

    @Schema(description = "Nest cursor")
    val nextCursor: String?,

    @Schema(description = "Has more")
    val hasMore: Boolean,
) {
    companion object {
        fun from(query: DatabaseQueryResponse): ArticleResponse = ArticleResponse(
            articles = query.results.map { page ->
                ArticleSummary(
                    id = page.id,
                    title = page.properties["Page"]?.title?.firstOrNull()?.plain_text.orEmpty(),
                    slug = page.properties["Slug"]?.rich_text?.firstOrNull()?.plain_text.orEmpty(),
                    date = page.properties["Date"]?.date?.start,
                    tags = page.properties["Tag"]?.multi_select?.map { it.name } ?: emptyList(),
                    emoji = page.icon?.emoji,
                )
            },
            nextCursor = query.next_cursor,
            hasMore = query.has_more,
        )
    }
}

data class ArticleSummary(
    val id: String,
    val title: String,
    val slug: String,
    val date: String?,
    val tags: List<String>,
    val emoji: String?,
)
