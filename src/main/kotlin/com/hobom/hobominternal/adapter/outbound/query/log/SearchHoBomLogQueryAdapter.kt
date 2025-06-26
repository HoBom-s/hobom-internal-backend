package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.port.outbound.log.SearchHoBomLogQueryPort
import com.hobom.hobominternal.shared.page.QueryResult
import org.springframework.stereotype.Component

@Component
class SearchHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : SearchHoBomLogQueryPort {
    override fun findBy(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLogQueryResult> {
        val hobomLogs = findByCriteria(criteria, page, size)
        return QueryResult(
            total = hobomLogs.total,
            items = hobomLogs.items.map { HoBomLogQueryResult.from(it) },
        )
    }

    private fun findByCriteria(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLog> = hobomLogQueryRepository
        .findFilteredLogs(criteria, page, size)
}
