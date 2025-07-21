package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.port.outbound.FindHoBomLogByIdQueryPort
import org.springframework.stereotype.Component

@Component
class FindHoBomLogByIdQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : FindHoBomLogByIdQueryPort {
    override fun findById(id: HoBomLogId): HoBomLogQueryResult {
        val hobomLog = findBy(id)
        return HoBomLogQueryResult.from(hobomLog)
    }

    private fun findBy(id: HoBomLogId): HoBomLog = hobomLogQueryRepository
        .findById(id)
}
