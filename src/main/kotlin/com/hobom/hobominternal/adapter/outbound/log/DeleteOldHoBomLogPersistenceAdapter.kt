package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLogCommandRepository
import com.hobom.hobominternal.domain.log.port.outbound.DeleteOldHoBomLogPersistencePort
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DeleteOldHoBomLogPersistenceAdapter(
    private val hobomLogCommandRepository: HoBomLogCommandRepository,
) : DeleteOldHoBomLogPersistencePort {
    override fun deleteBatch(threshold: LocalDateTime, batchSize: Int): Int {
        return hobomLogCommandRepository.deleteOlderThan(threshold, batchSize)
    }
}
