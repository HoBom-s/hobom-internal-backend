package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toMail
import com.hobom.hobominternal.domain.mail.port.outbound.SendMailPort
import com.hobom.hobominternal.domain.message.model.MessageType
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
