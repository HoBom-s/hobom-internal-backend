package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.EndpointErrorCount

interface TopErrorEndpointHoBomLogQueryPort {
    fun find(hours: Int, limit: Int): List<EndpointErrorCount>
}
