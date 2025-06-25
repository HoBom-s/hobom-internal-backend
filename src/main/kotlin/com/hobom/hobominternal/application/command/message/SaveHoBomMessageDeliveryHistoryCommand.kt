package com.hobom.hobominternal.application.command.message

import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.MessageType
import java.time.Instant

data class SaveHoBomMessageDeliveryHistoryCommand(
    val type: MessageType,
    val title: String,
    val body: String,
    val recipient: String,
    val senderId: String?,
    val sentAt: Instant,
)

fun SaveHoBomMessageDeliveryHistoryCommand.toRequest(): HoBomMessageDeliveryHistoryCreateRequest = HoBomMessageDeliveryHistoryCreateRequest(
    type = type,
    title = title,
    body = body,
    recipient = recipient,
    senderId = senderId,
    sentAt = sentAt,
)
