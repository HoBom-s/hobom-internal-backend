package com.hobom.hobominternal.domain.notion.port.inbound

import com.hobom.hobominternal.domain.notion.model.NotionBlockResult

interface GetNotionArticleBySlugUseCase {
    fun invoke(slug: String): NotionBlockResult
}
