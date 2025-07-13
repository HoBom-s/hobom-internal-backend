package com.hobom.hobominternal.port.inbound.dlq

import com.hobom.hobominternal.application.command.dlq.ManualSendMessageDlqCommand

interface ManualSendMessageDlqUseCase {
    fun invoke(command: ManualSendMessageDlqCommand)
}
