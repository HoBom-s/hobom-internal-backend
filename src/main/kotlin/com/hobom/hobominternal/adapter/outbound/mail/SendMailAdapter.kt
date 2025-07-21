package com.hobom.hobominternal.adapter.outbound.mail

import com.hobom.hobominternal.domain.mail.model.MailRequest
import com.hobom.hobominternal.domain.mail.port.outbound.SendMailPort
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
