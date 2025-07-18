package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.application.command.log.SaveLogCommand

interface SaveBulkLogUseCase {
    fun invoke(command: List<SaveLogCommand>)
}
