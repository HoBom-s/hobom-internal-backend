package com.hobom.hobominternal.adapter.inbound.dto.dlq

import com.hobom.hobominternal.application.command.dlq.ManualSendMessageDlqCommand
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.message.MessageType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Manual DLQ Send Retry")
data class ManualSendDlqMessageRequest(
    @Schema(description = "Message Title", example = "Today's menu recommendation")
    val title: String,

    @Schema(description = "Message Content", example = "Sushi")
    val body: String,

    @Schema(description = "Recipient", example = "foxmon1524@gmail.com")
    val recipient: String,

    @Schema(description = "Mail or Push", example = "MAIL_MESSAGE")
    val messageType: MessageType,
) {
    fun toCommand(id: DlqMessageId): ManualSendMessageDlqCommand = ManualSendMessageDlqCommand(
        id = id,
        title = title,
        body = body,
        recipient = recipient,
        messageType = messageType,
    )
}
