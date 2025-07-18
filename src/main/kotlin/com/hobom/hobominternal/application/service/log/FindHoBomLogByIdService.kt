package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.adapter.outbound.log.HoBomLogQueryResult
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.port.inbound.FindHoBomLogByIdUseCase
import com.hobom.hobominternal.domain.log.port.outbound.FindHoBomLogByIdQueryPort
import org.springframework.stereotype.Service

@Service
class FindHoBomLogByIdService(
    private val findHoBomLogByIdQueryPort: FindHoBomLogByIdQueryPort,
) : FindHoBomLogByIdUseCase {
    override fun invoke(id: HoBomLogId): HoBomLogQueryResult {
        return findById(id)
    }

    private fun findById(id: HoBomLogId): HoBomLogQueryResult = findHoBomLogByIdQueryPort
        .findById(id)
}
