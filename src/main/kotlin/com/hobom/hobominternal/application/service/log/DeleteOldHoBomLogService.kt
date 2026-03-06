package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.port.inbound.DeleteOldHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.DeleteOldHoBomLogPersistencePort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DeleteOldHoBomLogService(
    private val deleteOldHoBomLogPersistencePort: DeleteOldHoBomLogPersistencePort,
) : DeleteOldHoBomLogUseCase {
    companion object {
        private const val RETENTION_DAYS = 30L
        private const val BATCH_SIZE = 100
    }

    override fun invoke(): Long {
        val threshold = LocalDateTime.now().minusDays(RETENTION_DAYS)
        var totalDeleted = 0L

        while (true) {
            val deleted = deleteOldHoBomLogPersistencePort.deleteBatch(threshold, BATCH_SIZE)
            totalDeleted += deleted
            if (deleted < BATCH_SIZE) break
        }

        return totalDeleted
    }
}
