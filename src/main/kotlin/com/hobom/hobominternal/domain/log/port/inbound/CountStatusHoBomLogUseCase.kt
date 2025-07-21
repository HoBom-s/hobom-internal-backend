package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.domain.log.model.LogStatusCount

interface CountStatusHoBomLogUseCase {
    fun invoke(): List<LogStatusCount>
}
