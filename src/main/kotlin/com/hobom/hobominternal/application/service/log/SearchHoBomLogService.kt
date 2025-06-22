package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.port.inbound.log.SearchHoBomLogUseCase
import com.hobom.hobominternal.port.outbound.log.SearchHoBomLogQueryPort
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SearchHoBomLogService(
    private val searchHoBomLogQueryPort: SearchHoBomLogQueryPort,
) : SearchHoBomLogUseCase {
    override fun invoke(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLogQueryResult> {
        return findBy(criteria, pageable)
    }

    private fun findBy(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLogQueryResult> = searchHoBomLogQueryPort
        .findBy(criteria, pageable)
}
