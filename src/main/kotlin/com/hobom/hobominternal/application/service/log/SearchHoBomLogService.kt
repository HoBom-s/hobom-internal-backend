package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogSearchCriteria
import com.hobom.hobominternal.domain.log.port.inbound.SearchHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.SearchHoBomLogQueryPort
import com.hobom.hobominternal.shared.page.QueryResult
import org.springframework.stereotype.Service

@Service
class SearchHoBomLogService(
    private val hobomLogQueryPort: SearchHoBomLogQueryPort,
) : SearchHoBomLogUseCase {
    override fun invoke(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult> {
        return findBy(criteria, page, size)
    }

    private fun findBy(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult> = hobomLogQueryPort
        .findBy(criteria, page, size)
}
