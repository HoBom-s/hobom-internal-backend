package com.hobom.hobominternal.application.service.notion

import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionArticlesUseCase
import com.hobom.hobominternal.domain.notion.port.outbound.NotionQueryPort
import org.springframework.stereotype.Service

@Service
class GetNotionArticlesService(
    private val notionQueryPort: NotionQueryPort,
) : GetNotionArticlesUseCase {
    override fun invoke(pageSize: Int, cursor: String?): NotionArticlesResult =
        notionQueryPort.getArticles(pageSize, cursor)
}
