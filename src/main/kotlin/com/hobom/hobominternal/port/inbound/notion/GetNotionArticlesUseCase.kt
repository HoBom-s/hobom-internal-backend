package com.hobom.hobominternal.port.inbound.notion

import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse

interface GetNotionArticlesUseCase {
    fun invoke(pageSize: Int, cursor: String?): DatabaseQueryResponse
}
