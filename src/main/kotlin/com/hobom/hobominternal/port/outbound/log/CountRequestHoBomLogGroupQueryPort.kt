package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.domain.log.RequestCount

interface CountRequestHoBomLogGroupQueryPort {
    fun count(): List<RequestCount>
}
