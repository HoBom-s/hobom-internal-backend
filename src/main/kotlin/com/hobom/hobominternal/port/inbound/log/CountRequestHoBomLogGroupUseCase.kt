package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.domain.log.RequestCount

interface CountRequestHoBomLogGroupUseCase {
    fun invoke(): List<RequestCount>
}
