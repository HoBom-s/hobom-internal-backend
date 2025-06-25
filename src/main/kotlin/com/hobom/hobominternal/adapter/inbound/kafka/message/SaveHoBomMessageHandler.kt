package com.hobom.hobominternal.adapter.inbound.kafka.message

import com.hobom.hobominternal.application.command.message.SaveHoBomMessageDeliveryHistoryCommand
import com.hobom.hobominternal.infra.kafka.KafkaMessageHandler
import com.hobom.hobominternal.port.inbound.message.SaveHoBomMessageDeliveryHistoryUseCase
import org.springframework.stereotype.Component

@Component
class SaveHoBomMessageHandler(
    private val useCase: SaveHoBomMessageDeliveryHistoryUseCase,
) : KafkaMessageHandler<SaveHoBomMessageDeliveryHistoryCommand> {
    override fun handle(message: SaveHoBomMessageDeliveryHistoryCommand) {
        useCase.invoke(message)
    }
}
