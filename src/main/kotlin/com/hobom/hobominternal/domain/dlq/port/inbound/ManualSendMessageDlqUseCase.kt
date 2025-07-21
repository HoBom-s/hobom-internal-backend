package com.hobom.hobominternal.domain.dlq.port.inbound

import com.hobom.hobominternal.application.command.dlq.ManualSendMessageDlqCommand

interface ManualSendMessageDlqUseCase {
    fun invoke(command: ManualSendMessageDlqCommand)
}
