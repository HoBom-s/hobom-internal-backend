package com.hobom.hobominternal.adapter.outbound.notion

import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import com.hobom.hobominternal.infra.feign.notion.client.NotionFeignClient
import com.hobom.hobominternal.infra.feign.notion.dto.BlockChildrenResponse
import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import com.hobom.hobominternal.infra.feign.notion.dto.NotionBlock
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotionQueryAdapter(
    private val notionFeignClient: NotionFeignClient,
) : NotionQueryPort {
    @Value("\${notion.database}")
    private lateinit var databaseId: String

    override fun getDataBaseByRequest(request: NotionQueryRequest): DatabaseQueryResponse {
        return getDatabaseBy(databaseId, request)
    }

    override fun getBlockByPageId(id: String): BlockChildrenResponse {
        val all = mutableListOf<NotionBlock>()
        var cursor: String? = null
        var last: BlockChildrenResponse

        do {
            last = notionFeignClient.getBlockChildren(
                blockId = id,
                startCursor = cursor,
                pageSize = 100,
            )
            all += last.results
            cursor = last.next_cursor
        } while (last.has_more)

        return BlockChildrenResponse(
            results = all,
            has_more = false,
            next_cursor = null,
        )
    }

    private fun getDatabaseBy(databaseId: String, request: NotionQueryRequest): DatabaseQueryResponse = notionFeignClient
        .getDatabase(databaseId, request)
}
