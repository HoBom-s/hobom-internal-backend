package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogId

interface FindHoBomLogByIdUseCase {
    fun invoke(id: HoBomLogId): HoBomLogQueryResult
}
