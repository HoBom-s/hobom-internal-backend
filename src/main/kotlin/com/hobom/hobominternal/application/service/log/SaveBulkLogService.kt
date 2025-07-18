package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.application.command.log.toModel
import com.hobom.hobominternal.domain.log.port.inbound.SaveBulkLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.SaveHoBomLogPersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SaveBulkLogService(
    private val saveHoBomLogPort: SaveHoBomLogPersistencePort,
) : SaveBulkLogUseCase {
    @Transactional
    override fun invoke(command: List<SaveLogCommand>) {
        saveBulkLog(command)
    }

    private fun saveBulkLog(command: List<SaveLogCommand>) {
        saveHoBomLogPort.saveAll(command.map { it.toModel() })
    }
}
