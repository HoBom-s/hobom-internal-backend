package com.hobom.hobominternal.port.inbound.log

import com.hobom.hobominternal.application.command.log.SaveLogCommand

interface SaveBulkLogUseCase {
    fun invoke(command: List<SaveLogCommand>)
}
