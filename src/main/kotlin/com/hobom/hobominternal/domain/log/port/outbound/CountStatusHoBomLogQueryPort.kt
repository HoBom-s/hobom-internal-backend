package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.LogStatusCount

interface CountStatusHoBomLogQueryPort {
    fun count(): List<LogStatusCount>
}
