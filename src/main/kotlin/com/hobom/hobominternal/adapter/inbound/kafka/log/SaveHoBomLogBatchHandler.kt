package com.hobom.hobominternal.adapter.inbound.kafka.log

import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.domain.log.port.inbound.SaveBulkLogUseCase
import com.hobom.hobominternal.infra.kafka.KafkaBatchMessageHandler
import org.springframework.stereotype.Component

@Component
class SaveHoBomLogBatchHandler(
    private val useCase: SaveBulkLogUseCase,
) : KafkaBatchMessageHandler<SaveLogCommand> {
    override fun handle(messages: List<SaveLogCommand>) {
        println(messages)
        useCase.invoke(messages)
    }
}
