package com.hobom.hobominternal.adapter.outbound.persistence.mail

import com.hobom.hobominternal.domain.mail.MailRequest
import com.hobom.hobominternal.port.outbound.mail.SendMailPort
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class SendMailAdapter(
    private val mailSender: JavaMailSender,
) : SendMailPort {
    override fun send(request: MailRequest) {
        val message = SimpleMailMessage().apply {
            setTo(request.to)
            subject = request.title
            text = request.content
        }
        mailSender.send(message)
    }
}
