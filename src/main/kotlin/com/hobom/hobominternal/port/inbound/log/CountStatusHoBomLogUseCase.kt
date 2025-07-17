package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.domain.log.LogStatusCount

interface CountStatusHoBomLogUseCase {
    fun invoke(): List<LogStatusCount>
}
