package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.infra.repository.log.HoBomLogJooqRepository
import com.hobom.hobominternal.port.outbound.log.SearchHoBomLogQueryPort
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class SearchHoBomLogQueryAdapter(
    private val hoBomLogRepository: HoBomLogJooqRepository,
) : SearchHoBomLogQueryPort {
    override fun findBy(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLogQueryResult> {
        return findByCriteria(criteria, pageable).map { it.toQueryResult() }
    }

    private fun findByCriteria(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLog> = hoBomLogRepository
        .findFilteredLogs(criteria, pageable)
}
