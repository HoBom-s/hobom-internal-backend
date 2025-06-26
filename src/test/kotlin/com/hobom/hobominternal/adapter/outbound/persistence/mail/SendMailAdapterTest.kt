package com.hobom.hobominternal.adapter.outbound.persistence.mail

import com.hobom.hobominternal.domain.mail.MailRequest
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import kotlin.test.assertEquals

class SendMailAdapterTest {
    private val mailSender = mockk<JavaMailSender>(relaxed = true)
    private val adapter = SendMailAdapter(mailSender)

    @Test
    fun `should send mail using JavaMailSender`() {
        val request = MailRequest(
            to = "test@example.com",
            title = "제목입니다",
            content = "내용입니다",
        )

        adapter.send(request)

        val slot = slot<SimpleMailMessage>()
        verify { mailSender.send(capture(slot)) }

        val message = slot.captured
        assertEquals(request.to, message.to?.first())
        assertEquals(request.title, message.subject)
        assertEquals(request.content, message.text)
    }
}
