package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogSearchCriteria
import com.hobom.hobominternal.shared.page.QueryResult

interface SearchHoBomLogQueryPort {
    fun findBy(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult>
}
