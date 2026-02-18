package com.hobom.hobominternal.adapter.outbound.notion

import com.hobom.hobominternal.domain.notion.model.NotionArticle
import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import com.hobom.hobominternal.infra.feign.notion.client.NotionFeignClient
import com.hobom.hobominternal.infra.feign.notion.dto.NotionBlock
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest
import com.hobom.hobominternal.infra.feign.notion.util.NotionMarkdownFormatter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotionQueryAdapter(
    private val notionFeignClient: NotionFeignClient,
) : NotionQueryPort {
    @Value("\${notion.database}")
    private lateinit var databaseId: String

    override fun getArticles(pageSize: Int, cursor: String?): NotionArticlesResult {
        val request = NotionQueryRequest(
            page_size = pageSize,
            start_cursor = cursor,
            filter = mapOf(
                "property" to "Published",
                "checkbox" to mapOf("equals" to true),
            ),
            sorts = listOf(
                mapOf("property" to "Date", "direction" to "descending"),
            ),
        )
        val response = notionFeignClient.getDatabase(databaseId, request)
        return NotionArticlesResult(
            articles = response.results.map { page ->
                NotionArticle(
                    id = page.id,
                    title = page.properties["Page"]?.title?.firstOrNull()?.plain_text.orEmpty(),
                    slug = page.properties["Slug"]?.rich_text?.firstOrNull()?.plain_text.orEmpty(),
                    date = page.properties["Date"]?.date?.start,
                    tags = page.properties["Tag"]?.multi_select?.map { it.name } ?: emptyList(),
                    emoji = page.icon?.emoji,
                )
            },
            nextCursor = response.next_cursor,
            hasMore = response.has_more,
        )
    }

    override fun getBlockMarkdownByPageId(id: String): String {
        val all = mutableListOf<NotionBlock>()
        var cursor: String? = null
        var hasMore: Boolean

        do {
            val page = notionFeignClient.getBlockChildren(
                blockId = id,
                startCursor = cursor,
                pageSize = 100,
            )
            all += page.results
            cursor = page.next_cursor
            hasMore = page.has_more
        } while (hasMore)

        return NotionMarkdownFormatter.convertToMarkdown(all)
    }
}
