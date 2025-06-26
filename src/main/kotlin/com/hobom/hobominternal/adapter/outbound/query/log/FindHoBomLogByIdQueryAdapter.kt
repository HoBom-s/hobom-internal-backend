package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogId
import com.hobom.hobominternal.domain.log.HoBomLogQueryRepository
import com.hobom.hobominternal.port.outbound.log.FindHoBomLogByIdQueryPort
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
