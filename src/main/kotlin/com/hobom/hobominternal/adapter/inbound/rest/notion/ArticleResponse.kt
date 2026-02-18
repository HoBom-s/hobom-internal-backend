package com.hobom.hobominternal.adapter.inbound.rest.notion

import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult
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
        fun from(result: NotionArticlesResult): ArticleResponse = ArticleResponse(
            articles = result.articles.map { article ->
                ArticleSummary(
                    id = article.id,
                    title = article.title,
                    slug = article.slug,
                    date = article.date,
                    tags = article.tags,
                    emoji = article.emoji,
                )
            },
            nextCursor = result.nextCursor,
            hasMore = result.hasMore,
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
