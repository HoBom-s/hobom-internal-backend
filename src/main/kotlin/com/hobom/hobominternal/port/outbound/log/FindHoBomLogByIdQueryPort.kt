package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogId

interface FindHoBomLogByIdQueryPort {
    fun findById(id: HoBomLogId): HoBomLogQueryResult
}
