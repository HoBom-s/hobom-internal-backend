package com.hobom.hobominternal.infra.repository.message

import com.example.jooq.generated.Tables.MESSAGE_DELIVERY_HISTORIES
import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.MessageType
import org.assertj.core.api.Assertions.assertThat
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import java.time.Instant

class HoBomMessageDeliveryHistorySqlMapperTest {
    @Test
    fun `toInsertMap should generate correct SQL`() {
        val ctx = DSL.using(SQLDialect.MYSQL)
        val insert = ctx.insertInto(MESSAGE_DELIVERY_HISTORIES)
        val now = Instant.parse("2025-06-25T10:00:00Z")
        val request = HoBomMessageDeliveryHistoryCreateRequest(
            type = MessageType.PUSH_MESSAGE,
            title = "Hello",
            body = "World",
            recipient = "01012345678",
            senderId = "admin",
            sentAt = Instant.parse("2025-06-25T09:00:00Z"),
        )

        val query = HoBomMessageDeliveryHistorySqlMapper.toInsertMap(insert, request, now)

        assertThat(query.sql).contains("insert into `bear`.`message_delivery_histories`")
        assertThat(query.sql).contains("type", "title", "body", "recipient", "sender_id", "sent_at", "created_at", "updated_at")
    }
}
