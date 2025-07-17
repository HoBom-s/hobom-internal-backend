package com.hobom.hobominternal.adapter.outbound.query.log

import com.hobom.hobominternal.domain.log.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.RequestCount
import com.hobom.hobominternal.port.outbound.log.CountRequestHoBomLogGroupQueryPort
import org.springframework.stereotype.Component

@Component
class CountRequestHoBomLogGroupQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountRequestHoBomLogGroupQueryPort {
    override fun count(): List<RequestCount> {
        return hobomLogQueryRepository.countRequestsGroupedByMinute()
    }
}
