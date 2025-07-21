package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.RequestCount

interface CountRequestHoBomLogGroupQueryPort {
    fun count(): List<RequestCount>
}
