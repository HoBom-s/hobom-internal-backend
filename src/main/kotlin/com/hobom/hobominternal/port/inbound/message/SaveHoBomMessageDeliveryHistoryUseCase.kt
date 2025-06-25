package com.hobom.hobominternal.port.inbound.message

import com.hobom.hobominternal.application.command.message.SaveHoBomMessageDeliveryHistoryCommand

interface SaveHoBomMessageDeliveryHistoryUseCase {
    fun invoke(command: SaveHoBomMessageDeliveryHistoryCommand)
}
