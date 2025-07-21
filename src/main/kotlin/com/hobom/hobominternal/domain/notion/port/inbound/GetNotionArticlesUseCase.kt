package com.hobom.hobominternal.domain.notion.port.inbound

import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse

interface GetNotionArticlesUseCase {
    fun invoke(pageSize: Int, cursor: String?): DatabaseQueryResponse
}
