package com.hobom.hobominternal.port.inbound.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand

interface SaveHoBomMessageDeliveryHistoryUseCase {
    fun invoke(command: DeliverHoBomMessageCommand)
}
