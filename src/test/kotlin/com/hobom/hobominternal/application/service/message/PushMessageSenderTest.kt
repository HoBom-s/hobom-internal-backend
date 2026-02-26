package com.hobom.hobominternal.application.service.message

import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.domain.message.model.MessageType
import com.hobom.hobominternal.domain.notification.model.NotificationCategory
import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PushMessageSenderTest {
    private val sendNotificationPort = mockk<SendNotificationPort>(relaxed = true)
    private val sender = PushMessageSender(sendNotificationPort)

    @Test
    fun `supports should return true for PUSH_MESSAGE`() {
        assertTrue(sender.supports(MessageType.PUSH_MESSAGE))
    }

    @Test
    fun `supports should return false for MAIL_MESSAGE`() {
        assertFalse(sender.supports(MessageType.MAIL_MESSAGE))
    }

    @Test
    fun `send should delegate to SendNotificationPort with mapped request`() {
        val command = DeliverHoBomMessageCommand(
            type = MessageType.PUSH_MESSAGE,
            title = "알림 제목",
            body = "알림 본문",
            recipient = "hobom",
            senderId = "system",
            sentAt = Instant.now(),
        )

        sender.send(command)

        val slot = slot<NotificationRequest>()
        verify(exactly = 1) { sendNotificationPort.send(capture(slot)) }
        assertEquals(NotificationCategory.SYSTEM, slot.captured.category)
        assertEquals("hobom", slot.captured.recipient)
        assertEquals("알림 제목", slot.captured.title)
        assertEquals("알림 본문", slot.captured.body)
        assertEquals("system", slot.captured.senderId)
    }
}
