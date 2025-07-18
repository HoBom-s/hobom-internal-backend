package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.domain.message.model.MessageType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class SendHoBomMessageServiceTest {
    private val supportedStrategy = mockk<MessageSenderStrategy>(relaxed = true)
    private val unsupportedStrategy = mockk<MessageSenderStrategy>(relaxed = true)
    private val command = DeliverHoBomMessageCommand(
        type = MessageType.MAIL_MESSAGE,
        title = "테스트 제목",
        body = "테스트 본문",
        recipient = "test@example.com",
        senderId = "hobom",
        sentAt = Instant.now(),
    )

    @Test
    fun `should delegate to matching strategy`() {
        every { supportedStrategy.supports(MessageType.MAIL_MESSAGE) } returns true
        every { unsupportedStrategy.supports(any()) } returns false

        val service = SendHoBomMessageService(listOf(unsupportedStrategy, supportedStrategy))

        service.invoke(command)

        verify(exactly = 1) { supportedStrategy.send(command) }
        verify(exactly = 0) { unsupportedStrategy.send(any()) }
    }

    @Test
    fun `should throw exception when no strategy supports the type`() {
        every { supportedStrategy.supports(any()) } returns false
        val service = SendHoBomMessageService(listOf(supportedStrategy))

        val ex = assertThrows(UnsupportedOperationException::class.java) {
            service.invoke(command)
        }
        assertTrue(ex.message!!.contains("Unsupported message type"))
    }
}
