package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.LogStatusCount
import com.hobom.hobominternal.port.outbound.log.CountStatusHoBomLogQueryPort
import org.springframework.stereotype.Component

@Component
class CountStatusHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountStatusHoBomLogQueryPort {
    override fun count(): List<LogStatusCount> {
        return hobomLogQueryRepository.countStatusCode()
    }
}
