package com.hobom.hobominternal.domain.message.port.inbound

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand

interface SendHoBomMessageUseCase {
    fun invoke(command: DeliverHoBomMessageCommand)
}
