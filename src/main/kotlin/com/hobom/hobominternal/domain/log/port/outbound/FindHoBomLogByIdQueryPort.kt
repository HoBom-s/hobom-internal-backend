package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogId

interface FindHoBomLogByIdQueryPort {
    fun findById(id: HoBomLogId): HoBomLogQueryResult
}
