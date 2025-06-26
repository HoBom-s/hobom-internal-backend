package com.hobom.hobominternal.port.inbound.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand

interface SendHoBomMessageUseCase {
    fun invoke(command: DeliverHoBomMessageCommand)
}
