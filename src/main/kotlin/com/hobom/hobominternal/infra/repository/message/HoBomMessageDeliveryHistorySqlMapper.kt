package com.hobom.hobominternal.infra.repository.message

import com.example.jooq.generated.Tables.MESSAGE_DELIVERY_HISTORIES
import com.example.jooq.generated.tables.records.MessageDeliveryHistoriesRecord
import com.hobom.hobominternal.domain.message.model.HoBomMessageDeliveryHistoryCreateRequest
import org.jooq.InsertSetStep
import org.jooq.Query
import java.time.Instant
import java.time.ZoneOffset

object HoBomMessageDeliveryHistorySqlMapper {
    fun toInsertMap(
        insert: InsertSetStep<MessageDeliveryHistoriesRecord>,
        request: HoBomMessageDeliveryHistoryCreateRequest,
        now: Instant,
    ): Query {
        return insert
            .set(MESSAGE_DELIVERY_HISTORIES.TYPE, request.type.name)
            .set(MESSAGE_DELIVERY_HISTORIES.TITLE, request.title)
            .set(MESSAGE_DELIVERY_HISTORIES.BODY, request.body)
            .set(MESSAGE_DELIVERY_HISTORIES.RECIPIENT, request.recipient)
            .set(MESSAGE_DELIVERY_HISTORIES.SENDER_ID, request.senderId)
            .set(MESSAGE_DELIVERY_HISTORIES.SENT_AT, request.sentAt.atOffset(ZoneOffset.UTC).toLocalDateTime())
            .set(MESSAGE_DELIVERY_HISTORIES.CREATED_AT, now.atOffset(ZoneOffset.UTC).toLocalDateTime())
            .set(MESSAGE_DELIVERY_HISTORIES.UPDATED_AT, now.atOffset(ZoneOffset.UTC).toLocalDateTime())
    }
}
