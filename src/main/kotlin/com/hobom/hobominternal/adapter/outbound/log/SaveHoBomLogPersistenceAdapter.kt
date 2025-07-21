package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogJdbcRepository
import com.hobom.hobominternal.domain.log.port.outbound.SaveHoBomLogPersistencePort
import org.springframework.stereotype.Component

@Component
class SaveHoBomLogPersistenceAdapter(
    private val hobomLogJdbcRepository: HoBomLogJdbcRepository,
) : SaveHoBomLogPersistencePort {
    override fun saveAll(logs: List<HoBomLog>) {
        hobomLogJdbcRepository.batchInsert(logs)
    }
}
