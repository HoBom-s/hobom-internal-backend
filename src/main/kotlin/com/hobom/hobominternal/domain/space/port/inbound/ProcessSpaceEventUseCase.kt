package com.hobom.hobominternal.domain.space.port.inbound

import com.hobom.hobominternal.application.command.space.ProcessSpaceEventCommand

interface ProcessSpaceEventUseCase {
    fun invoke(command: ProcessSpaceEventCommand)
}
