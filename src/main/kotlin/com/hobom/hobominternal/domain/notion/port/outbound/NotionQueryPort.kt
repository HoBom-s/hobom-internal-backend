package com.hobom.hobominternal.domain.notion.port.outbound

import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult

interface NotionQueryPort {
    fun getArticles(pageSize: Int, cursor: String?): NotionArticlesResult

    fun getBlockMarkdownByPageId(id: String): String
}
