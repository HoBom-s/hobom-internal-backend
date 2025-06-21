package com.hobom.hobominternal.port.outbound.log

import com.hobom.hobominternal.domain.log.HoBomLogModel

interface SaveHoBomLogPort {
    fun saveAll(logs: List<HoBomLogModel>)
}
