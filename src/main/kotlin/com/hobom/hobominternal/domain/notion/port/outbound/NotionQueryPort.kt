package com.hobom.hobominternal.domain.notion.port.outbound

import com.hobom.hobominternal.domain.notion.model.NotionArticlesResult
import com.hobom.hobominternal.domain.notion.model.NotionBlockResult

interface NotionQueryPort {
    fun getArticles(pageSize: Int, cursor: String?): NotionArticlesResult

    fun getBlockByPageId(id: String): NotionBlockResult

    fun getBlockBySlug(slug: String): NotionBlockResult
}
