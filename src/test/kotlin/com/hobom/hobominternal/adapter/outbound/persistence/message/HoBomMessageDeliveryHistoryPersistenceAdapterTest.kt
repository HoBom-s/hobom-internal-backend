package com.hobom.hobominternal.adapter.outbound.persistence.message

import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryPersistenceRepository
import com.hobom.hobominternal.domain.message.MessageType
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class HoBomMessageDeliveryHistoryPersistenceAdapterTest {
    private lateinit var repository: HoBomMessageDeliveryHistoryPersistenceRepository
    private lateinit var adapter: HoBomMessageDeliveryHistoryPersistenceAdapter

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        adapter = HoBomMessageDeliveryHistoryPersistenceAdapter(repository)
    }

    @Test
    fun `save() should delegate to repository`() {
        val request = HoBomMessageDeliveryHistoryCreateRequest(
            type = MessageType.MAIL_MESSAGE,
            title = "test title",
            body = "test body",
            recipient = "recipient@example.com",
            senderId = "sender-123",
            sentAt = Instant.now(),
        )

        adapter.save(request)

        verify { repository.save(request) }
    }
}
