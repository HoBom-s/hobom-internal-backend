package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.LogLevelCount

interface CountLevelHoBomLogQueryPort {
    fun count(hours: Int): List<LogLevelCount>
}
