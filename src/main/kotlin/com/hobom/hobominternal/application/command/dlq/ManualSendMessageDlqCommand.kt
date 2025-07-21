package com.hobom.hobominternal.application.command.dlq

import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.message.model.MessageType
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
