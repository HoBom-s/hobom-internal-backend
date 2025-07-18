package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionArticlesUseCase
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest
import org.springframework.stereotype.Service

@Service
class GetNotionArticlesService(
    private val notionQueryPort: NotionQueryPort,
) : GetNotionArticlesUseCase {
    override fun invoke(pageSize: Int, cursor: String?): DatabaseQueryResponse {
        val request = buildRequest(pageSize, cursor)

        return notionQueryPort.getDataBaseByRequest(request)
    }

    private fun buildRequest(pageSize: Int, cursor: String?): NotionQueryRequest = NotionQueryRequest(
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
}
