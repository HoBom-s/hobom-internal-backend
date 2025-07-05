package com.hobom.hobominternal.port.outbound.notion

import com.hobom.hobominternal.infra.feign.notion.dto.BlockChildrenResponse
import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest

interface NotionQueryPort {
    fun getDataBaseByRequest(request: NotionQueryRequest): DatabaseQueryResponse

    fun getBlockByPageId(id: String): BlockChildrenResponse
}
