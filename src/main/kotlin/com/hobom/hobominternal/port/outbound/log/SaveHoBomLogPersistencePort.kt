package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.domain.log.HoBomLogModel

interface SaveHoBomLogPersistencePort {
    fun saveAll(logs: List<HoBomLogModel>)
}
