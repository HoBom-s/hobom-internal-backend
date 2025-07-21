package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.domain.log.model.RequestCount

interface CountRequestHoBomLogGroupUseCase {
    fun invoke(): List<RequestCount>
}
