package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.HoBomLog

interface SaveHoBomLogPersistencePort {
    fun saveAll(logs: List<HoBomLog>)
}
