package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.EndpointErrorCount
import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.port.outbound.TopErrorEndpointHoBomLogQueryPort
import org.springframework.stereotype.Component

@Component
class TopErrorEndpointHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : TopErrorEndpointHoBomLogQueryPort {
    override fun find(hours: Int, limit: Int): List<EndpointErrorCount> {
        return hobomLogQueryRepository.topErrorEndpoints(hours, limit)
    }
}
