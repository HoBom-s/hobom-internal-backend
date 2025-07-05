package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.infra.feign.notion.util.NotionMarkdownFormatter
import com.hobom.hobominternal.port.inbound.notion.GetNotionBlockUseCase
import com.hobom.hobominternal.port.outbound.notion.NotionQueryPort
import org.springframework.stereotype.Service

@Service
class GetNotionBlockService(
    private val notionQueryPort: NotionQueryPort,
) : GetNotionBlockUseCase {
    override fun invoke(id: String): String {
        val blockChildren = notionQueryPort.getBlockByPageId(id)
        val markdownContents = NotionMarkdownFormatter.convertToMarkdown(blockChildren.results)

        return markdownContents
    }
}
