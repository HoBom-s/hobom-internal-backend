package com.hobom.hobominternal.application.service.message.strategy

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toMail
import com.hobom.hobominternal.domain.message.MessageType
import com.hobom.hobominternal.port.outbound.mail.SendMailPort
import org.springframework.stereotype.Component

@Component
class MailMessageSender(
    private val sendMailPort: SendMailPort,
) : MessageSenderStrategy {
    override fun supports(type: MessageType): Boolean {
        return type == MessageType.MAIL_MESSAGE
    }

    override fun send(command: DeliverHoBomMessageCommand) {
        sendMailPort.send(command.toMail())
    }
}
