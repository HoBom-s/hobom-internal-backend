package com.hobom.hobominternal.application.service.message.strategy

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.domain.message.MessageType

interface MessageSenderStrategy {
    fun supports(type: MessageType): Boolean

    fun send(command: DeliverHoBomMessageCommand)
}
