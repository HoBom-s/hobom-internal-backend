package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.domain.log.model.LogLevelCount

interface CountLevelHoBomLogUseCase {
    fun invoke(hours: Int): List<LogLevelCount>
}
