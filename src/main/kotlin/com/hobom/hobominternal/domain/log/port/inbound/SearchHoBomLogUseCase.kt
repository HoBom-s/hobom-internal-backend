package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogSearchCriteria
import com.hobom.hobominternal.shared.page.QueryResult

interface SearchHoBomLogUseCase {
    fun invoke(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult>
}
