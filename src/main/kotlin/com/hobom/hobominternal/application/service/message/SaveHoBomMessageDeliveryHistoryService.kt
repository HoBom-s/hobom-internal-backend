package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toRequest
import com.hobom.hobominternal.port.inbound.message.SaveHoBomMessageDeliveryHistoryUseCase
import com.hobom.hobominternal.port.outbound.message.HoBomMessageDeliveryHistoryPersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SaveHoBomMessageDeliveryHistoryService(
    private val hobomMessageDeliveryHistoryPersistencePort: HoBomMessageDeliveryHistoryPersistencePort,
) : SaveHoBomMessageDeliveryHistoryUseCase {
    @Transactional
    override fun invoke(command: DeliverHoBomMessageCommand) {
        hobomMessageDeliveryHistoryPersistencePort.save(command.toRequest())
    }
}
