package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SearchHoBomLogUseCase {
    fun invoke(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLogQueryResult>
}
