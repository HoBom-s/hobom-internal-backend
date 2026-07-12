package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.model.EndpointErrorCount
import com.hobom.hobominternal.domain.log.port.inbound.TopErrorEndpointHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.TopErrorEndpointHoBomLogQueryPort
import org.springframework.stereotype.Service

@Service
class TopErrorEndpointHoBomLogService(
    private val topErrorEndpointHoBomLogQueryPort: TopErrorEndpointHoBomLogQueryPort,
) : TopErrorEndpointHoBomLogUseCase {
    override fun invoke(hours: Int, limit: Int): List<EndpointErrorCount> {
        return topErrorEndpointHoBomLogQueryPort.find(hours, limit)
    }
}
