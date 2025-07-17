package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.domain.log.LogStatusCount

interface CountStatusHoBomLogQueryPort {
    fun count(): List<LogStatusCount>
}
