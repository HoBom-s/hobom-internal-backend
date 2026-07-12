package com.hobom.hobominternal.infra.repository.dlq

import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqStatus
import org.jooq.DSLContext
import org.jooq.InsertSetStep
import org.jooq.JSON
import org.jooq.Query
import org.jooq.generated.tables.records.MessageDlqsRecord
import org.jooq.generated.tables.references.MESSAGE_DLQS
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
            .set(MESSAGE_DLQS.VALUE, JSON.valueOf(request.value))
            .set(MESSAGE_DLQS.TRACE_ID, request.traceId)
            .set(MESSAGE_DLQS.MESSAGE_TYPE, request.messageType)
            .set(MESSAGE_DLQS.ERROR_MESSAGE, request.errorMessage)
            .set(MESSAGE_DLQS.STATUS, request.status.value)
            .set(MESSAGE_DLQS.RETRY_COUNT, request.retryCount)
            .set(MESSAGE_DLQS.LAST_ATTEMPTED_AT, request.lastAttemptedAt?.atOffset(ZoneOffset.UTC))
    }

    fun upsert(
        dsl: DSLContext,
        id: Long,
        request: DlqMessageCreateRequest,
    ): Int {
        return dsl.insertInto(MESSAGE_DLQS)
            .set(MESSAGE_DLQS.ID, id)
            .set(MESSAGE_DLQS.TOPIC, request.topic)
            .set(MESSAGE_DLQS.PARTITION, request.partition)
            .set(MESSAGE_DLQS.KAFKA_OFFSET, request.kafkaOffset)
            .set(MESSAGE_DLQS.KEY, request.key)
            .set(MESSAGE_DLQS.VALUE, JSON.valueOf(request.value))
            .set(MESSAGE_DLQS.TRACE_ID, request.traceId)
            .set(MESSAGE_DLQS.MESSAGE_TYPE, request.messageType)
            .set(MESSAGE_DLQS.ERROR_MESSAGE, request.errorMessage)
            .set(MESSAGE_DLQS.STATUS, request.status.value)
            .set(MESSAGE_DLQS.RETRY_COUNT, request.retryCount)
            .set(MESSAGE_DLQS.LAST_ATTEMPTED_AT, request.lastAttemptedAt?.atOffset(ZoneOffset.UTC))
            .onConflict(MESSAGE_DLQS.ID)
            .doUpdate()
            .set(MESSAGE_DLQS.STATUS, request.status.value)
            .set(MESSAGE_DLQS.ERROR_MESSAGE, request.errorMessage)
            .set(MESSAGE_DLQS.RETRY_COUNT, request.retryCount)
            .set(MESSAGE_DLQS.LAST_ATTEMPTED_AT, request.lastAttemptedAt?.atOffset(ZoneOffset.UTC))
            .execute()
    }
}

fun MessageDlqsRecord.toDomain(): DlqMessage = DlqMessage(
    id = this.id?.let { DlqMessageId(it) } ?: error("message_dlqs.id must not be null"),
    topic = this.topic ?: error("message_dlqs.topic must not be null for id=${this.id}"),
    partition = this.partition ?: error("message_dlqs.partition must not be null for id=${this.id}"),
    kafkaOffset = this.kafkaOffset ?: error("message_dlqs.kafka_offset must not be null for id=${this.id}"),
    key = this.key,
    value = this.value?.toString() ?: error("message_dlqs.value must not be null for id=${this.id}"),
    traceId = this.traceId,
    messageType = this.messageType,
    errorMessage = this.errorMessage,
    retryCount = this.retryCount ?: error("message_dlqs.retry_count must not be null for id=${this.id}"),
    status = DlqStatus.valueOf(this.status ?: error("message_dlqs.status must not be null for id=${this.id}")),
    lastAttemptedAt = this.lastAttemptedAt?.toInstant(),
    createdAt = this.createdAt?.toInstant() ?: error("message_dlqs.created_at must not be null for id=${this.id}"),
)
