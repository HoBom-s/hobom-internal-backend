package com.hobom.hobominternal.domain.log.model

import com.hobom.hobominternal.shared.page.QueryResult

interface HoBomLogQueryRepository {
    fun findFilteredLogs(criteria: HoBomLogSearchCriteria, page: Int, size: Int): QueryResult<HoBomLog>

    fun findById(id: HoBomLogId): HoBomLog

    fun countStatusCode(hours: Int): List<LogStatusCount>

    fun countRequestsGroupedByMinute(hours: Int): List<RequestCount>

    fun countByLevel(hours: Int): List<LogLevelCount>

    fun countByServiceType(hours: Int): List<ServiceTypeCount>

    fun topErrorEndpoints(hours: Int, limit: Int): List<EndpointErrorCount>
}
