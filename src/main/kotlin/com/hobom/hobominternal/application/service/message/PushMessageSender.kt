package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toNotification
import com.hobom.hobominternal.domain.message.model.MessageType
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import org.springframework.stereotype.Component

@Component
class PushMessageSender(
    private val sendNotificationPort: SendNotificationPort,
) : MessageSenderStrategy {
    override fun supports(type: MessageType): Boolean {
        return type == MessageType.PUSH_MESSAGE
    }

    override fun send(command: DeliverHoBomMessageCommand) {
        sendNotificationPort.send(command.toNotification())
    }
}
