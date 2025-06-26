package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.service.message.strategy.MessageSenderStrategy
import com.hobom.hobominternal.port.inbound.message.SendHoBomMessageUseCase
import org.springframework.stereotype.Service

@Service
class SendHoBomMessageService(
    private val strategies: List<MessageSenderStrategy>,
) : SendHoBomMessageUseCase {
    override fun invoke(command: DeliverHoBomMessageCommand) {
        strategies
            .firstOrNull { it.supports(command.type) }
            ?.send(command)
            ?: throw UnsupportedOperationException("Unsupported message type: ${command.type}")
    }
}
