package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toNotification
import com.hobom.hobominternal.domain.message.model.MessageType
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PushMessageSender(
    private val sendNotificationPort: SendNotificationPort,
) : MessageSenderStrategy {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun supports(type: MessageType): Boolean {
        return type == MessageType.PUSH_MESSAGE
    }

    override fun send(command: DeliverHoBomMessageCommand) {
        log.info("Sending push notification to recipient: {}, title: {}", command.recipient, command.title)
        sendNotificationPort.send(command.toNotification())
        log.info("Push notification sent successfully to recipient: {}", command.recipient)
    }
}
