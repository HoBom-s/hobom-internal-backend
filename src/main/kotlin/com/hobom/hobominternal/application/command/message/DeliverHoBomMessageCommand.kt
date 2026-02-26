package com.hobom.hobominternal.application.command.message

import com.hobom.hobominternal.domain.mail.model.MailRequest
import com.hobom.hobominternal.domain.message.model.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.model.MessageType
import com.hobom.hobominternal.domain.notification.model.NotificationCategory
import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import java.time.Instant

data class DeliverHoBomMessageCommand(
    val type: MessageType,
    val title: String,
    val body: String,
    val recipient: String,
    val senderId: String?,
    val sentAt: Instant,
    val category: NotificationCategory? = null,
)

fun DeliverHoBomMessageCommand.toRequest(): HoBomMessageDeliveryHistoryCreateRequest = HoBomMessageDeliveryHistoryCreateRequest(
    type = type,
    title = title,
    body = body,
    recipient = recipient,
    senderId = senderId,
    sentAt = sentAt,
)

fun DeliverHoBomMessageCommand.toMail(): MailRequest = MailRequest(
    to = recipient,
    title = title,
    content = body,
)

fun DeliverHoBomMessageCommand.toNotification(): NotificationRequest = NotificationRequest(
    category = category ?: NotificationCategory.SYSTEM,
    recipient = recipient,
    title = title,
    body = body,
    senderId = senderId,
)
