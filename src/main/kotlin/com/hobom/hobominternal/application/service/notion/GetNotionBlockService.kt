package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.domain.notion.model.NotionBlockResult
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionBlockUseCase
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import org.springframework.stereotype.Service

@Service
class GetNotionBlockService(
    private val notionQueryPort: NotionQueryPort,
) : GetNotionBlockUseCase {
    override fun invoke(id: String): NotionBlockResult = notionQueryPort.getBlockByPageId(id)
}
