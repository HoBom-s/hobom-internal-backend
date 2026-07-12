package com.hobom.hobominternal.adapter.inbound.kafka.space

import com.hobom.hobominternal.application.command.space.ProcessSpaceEventCommand
import com.hobom.hobominternal.domain.space.port.inbound.ProcessSpaceEventUseCase
import com.hobom.hobominternal.infra.kafka.KafkaMessageHandler
import org.springframework.stereotype.Component

@Component
class HandleSpaceEventHandler(
    private val processSpaceEventUseCase: ProcessSpaceEventUseCase,
) : KafkaMessageHandler<ProcessSpaceEventCommand> {
    override fun handle(message: ProcessSpaceEventCommand) {
        processSpaceEventUseCase.invoke(message)
    }
}
