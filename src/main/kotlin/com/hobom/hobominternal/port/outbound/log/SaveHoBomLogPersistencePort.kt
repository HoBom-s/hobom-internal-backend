package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.domain.log.HoBomLog

interface SaveHoBomLogPersistencePort {
    fun saveAll(logs: List<HoBomLog>)
}
