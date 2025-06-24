package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.shared.page.QueryResult

interface SearchHoBomLogUseCase {
    fun invoke(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult>
}
