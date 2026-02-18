package com.hobom.hobominternal.domain.notion.port.inbound

import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult

interface GetNotionArticlesUseCase {
    fun invoke(pageSize: Int, cursor: String?): NotionArticlesResult
}
