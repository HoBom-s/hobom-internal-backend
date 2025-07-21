package com.hobom.hobominternal.domain.message.port.inbound

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand

interface SaveHoBomMessageDeliveryHistoryUseCase {
    fun invoke(command: DeliverHoBomMessageCommand)
}
