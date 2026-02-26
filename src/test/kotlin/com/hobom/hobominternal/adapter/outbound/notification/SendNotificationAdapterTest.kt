package com.hobom.hobominternal.adapter.outbound.notification

import com.hobom.hobominternal.domain.notification.model.NotificationCategory
import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import com.hobom.hobominternal.infra.feign.hobom.HoBomBackendFeignClient
import com.hobom.hobominternal.infra.feign.hobom.dto.CreateNotificationRequest
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SendNotificationAdapterTest {
    private val feignClient = mockk<HoBomBackendFeignClient>(relaxed = true)
    private val adapter = SendNotificationAdapter(feignClient)

    @Test
    fun `should call Feign client with correct request`() {
        val request = NotificationRequest(
            category = NotificationCategory.SYSTEM,
            recipient = "hobom",
            title = "제목",
            body = "내용",
            senderId = "system",
        )

        adapter.send(request)

        val slot = slot<CreateNotificationRequest>()
        verify(exactly = 1) { feignClient.createNotification(capture(slot)) }
        assertEquals("SYSTEM", slot.captured.category)
        assertEquals("hobom", slot.captured.recipient)
        assertEquals("제목", slot.captured.title)
        assertEquals("내용", slot.captured.body)
        assertEquals("system", slot.captured.senderId)
    }
}
