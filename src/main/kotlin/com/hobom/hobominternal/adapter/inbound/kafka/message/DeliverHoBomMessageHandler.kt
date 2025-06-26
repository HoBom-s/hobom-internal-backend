package com.hobom.hobominternal.adapter.inbound.kafka.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.infra.kafka.KafkaMessageHandler
import com.hobom.hobominternal.port.inbound.message.SaveHoBomMessageDeliveryHistoryUseCase
import com.hobom.hobominternal.port.inbound.message.SendHoBomMessageUseCase
import org.springframework.stereotype.Component

@Component
class DeliverHoBomMessageHandler(
    private val saveHoBomMessageDeliveryHistoryUseCase: SaveHoBomMessageDeliveryHistoryUseCase,
    private val sendHoBomMessageUseCase: SendHoBomMessageUseCase,
) : KafkaMessageHandler<DeliverHoBomMessageCommand> {
    override fun handle(message: DeliverHoBomMessageCommand) {
        sendHoBomMessageUseCase.invoke(message)
        saveHoBomMessageDeliveryHistoryUseCase.invoke(message)
    }
}
