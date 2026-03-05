package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.domain.log.model.EndpointErrorCount

interface TopErrorEndpointHoBomLogUseCase {
    fun invoke(hours: Int, limit: Int): List<EndpointErrorCount>
}
