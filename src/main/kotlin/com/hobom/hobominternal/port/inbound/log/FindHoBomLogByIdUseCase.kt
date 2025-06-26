package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.adapter.outbound.query.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.HoBomLogId

interface FindHoBomLogByIdUseCase {
    fun invoke(id: HoBomLogId): HoBomLogQueryResult
}
