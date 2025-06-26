package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogJdbcRepository
import com.hobom.hobominternal.port.outbound.log.SaveHoBomLogPersistencePort
import org.springframework.stereotype.Component

@Component
class SaveHoBomLogPersistenceAdapter(
    private val hobomLogJdbcRepository: HoBomLogJdbcRepository,
) : SaveHoBomLogPersistencePort {
    override fun saveAll(logs: List<HoBomLog>) {
        hobomLogJdbcRepository.batchInsert(logs)
    }
}
