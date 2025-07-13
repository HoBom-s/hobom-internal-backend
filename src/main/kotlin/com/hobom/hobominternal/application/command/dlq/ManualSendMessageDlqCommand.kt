package com.hobom.hobominternal.application.command.dlq

import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.message.MessageType
import java.time.Instant

data class ManualSendMessageDlqCommand(
    val id: DlqMessageId,
    val title: String,
    val body: String,
    val recipient: String,
    val messageType: MessageType,
    val senderId: String = "system",
    val sentAt: Instant = Instant.now(),
)
