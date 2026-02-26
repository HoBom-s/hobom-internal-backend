package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.command.message.toMail
import com.hobom.hobominternal.domain.mail.port.outbound.SendMailPort
import com.hobom.hobominternal.domain.message.model.MessageType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MailMessageSender(
    private val sendMailPort: SendMailPort,
) : MessageSenderStrategy {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun supports(type: MessageType): Boolean {
        return type == MessageType.MAIL_MESSAGE
    }

    override fun send(command: DeliverHoBomMessageCommand) {
        if (!isValidEmail(command.recipient)) {
            log.warn("Invalid email address: {}, skipping mail delivery", command.recipient)
            return
        }
        sendMailPort.send(command.toMail())
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }
}
