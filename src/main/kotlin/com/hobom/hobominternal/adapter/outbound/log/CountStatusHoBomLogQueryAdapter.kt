package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.LogStatusCount
import com.hobom.hobominternal.domain.log.port.outbound.CountStatusHoBomLogQueryPort
import org.springframework.stereotype.Component

@Component
class CountStatusHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountStatusHoBomLogQueryPort {
    override fun count(): List<LogStatusCount> {
        return hobomLogQueryRepository.countStatusCode()
    }
}
