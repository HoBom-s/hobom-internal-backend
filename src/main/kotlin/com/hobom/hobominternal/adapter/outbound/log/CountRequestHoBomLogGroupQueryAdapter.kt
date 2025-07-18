package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.RequestCount
import com.hobom.hobominternal.domain.log.port.outbound.CountRequestHoBomLogGroupQueryPort
import org.springframework.stereotype.Component

@Component
class CountRequestHoBomLogGroupQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountRequestHoBomLogGroupQueryPort {
    override fun count(): List<RequestCount> {
        return hobomLogQueryRepository.countRequestsGroupedByMinute()
    }
}
