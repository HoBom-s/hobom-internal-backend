package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.domain.notion.model.NotionBlockResult
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionArticleBySlugUseCase
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import org.springframework.stereotype.Service

@Service
class GetNotionArticleBySlugService(
    private val notionQueryPort: NotionQueryPort,
) : GetNotionArticleBySlugUseCase {
    override fun invoke(slug: String): NotionBlockResult = notionQueryPort.getBlockBySlug(slug)
}
