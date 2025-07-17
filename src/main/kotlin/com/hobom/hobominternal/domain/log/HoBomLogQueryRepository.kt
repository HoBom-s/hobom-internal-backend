package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.shared.page.QueryResult

interface HoBomLogQueryRepository {
    fun findFilteredLogs(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLog>

    fun findById(id: HoBomLogId): HoBomLog

    fun countStatusCode(): List<LogStatusCount>

    fun countRequestsGroupedByMinute(): List<RequestCount>
}
