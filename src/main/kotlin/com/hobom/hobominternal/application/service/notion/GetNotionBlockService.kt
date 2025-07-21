package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionBlockUseCase
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import com.hobom.hobominternal.infra.feign.notion.util.NotionMarkdownFormatter
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
