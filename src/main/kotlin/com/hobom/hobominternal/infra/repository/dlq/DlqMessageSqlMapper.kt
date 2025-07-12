package com.hobom.hobominternal.infra.repository.dlq

import com.example.jooq.generated.Tables.MESSAGE_DLQS
import com.example.jooq.generated.tables.records.MessageDlqsRecord
import com.hobom.hobominternal.domain.dlq.DlqMessageCreateRequest
import org.jooq.InsertSetStep
import org.jooq.Query
import java.time.ZoneOffset

object DlqMessageSqlMapper {
    fun toInsertMap(
        insert: InsertSetStep<MessageDlqsRecord>,
        request: DlqMessageCreateRequest,
    ): Query {
        return insert
            .set(MESSAGE_DLQS.TOPIC, request.topic)
            .set(MESSAGE_DLQS.PARTITION, request.partition)
            .set(MESSAGE_DLQS.KAFKA_OFFSET, request.kafkaOffset)
            .set(MESSAGE_DLQS.KEY, request.key)
            .set(MESSAGE_DLQS.VALUE, org.jooq.JSONB.valueOf(request.value))
            .set(MESSAGE_DLQS.TRACE_ID, request.traceId)
            .set(MESSAGE_DLQS.MESSAGE_TYPE, request.messageType)
            .set(MESSAGE_DLQS.ERROR_MESSAGE, request.errorMessage)
            .set(MESSAGE_DLQS.STATUS, request.status.value)
            .set(MESSAGE_DLQS.RETRY_COUNT, request.retryCount)
            .set(MESSAGE_DLQS.LAST_ATTEMPTED_AT, request.lastAttemptedAt?.atOffset(ZoneOffset.UTC))
    }
}
