package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SearchHoBomLogQueryPort {
    fun findBy(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLogQueryResult>
}
