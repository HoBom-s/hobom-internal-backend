package com.hobom.hobominternal.infra.repository.dlq

import com.example.jooq.generated.Tables.MESSAGE_DLQS
import com.example.jooq.generated.tables.MessageDlqs
import com.example.jooq.generated.tables.records.MessageDlqsRecord
import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqStatus
import org.jooq.DSLContext
import org.jooq.InsertSetStep
import org.jooq.JSONB
import org.jooq.Query
import org.jooq.Record
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
            .set(MESSAGE_DLQS.VALUE, JSONB.valueOf(request.value))
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
            .set(MESSAGE_DLQS.VALUE, JSONB.valueOf(request.value))
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

fun Record.toDomain(): DlqMessage = DlqMessage(
    id = DlqMessageId(this[MessageDlqs.MESSAGE_DLQS.ID]),
    topic = this[MessageDlqs.MESSAGE_DLQS.TOPIC]!!,
    partition = this[MessageDlqs.MESSAGE_DLQS.PARTITION]!!,
    kafkaOffset = this[MessageDlqs.MESSAGE_DLQS.KAFKA_OFFSET]!!,
    key = this[MessageDlqs.MESSAGE_DLQS.KEY],
    value = this[MessageDlqs.MESSAGE_DLQS.VALUE]!!.toString(),
    traceId = this[MessageDlqs.MESSAGE_DLQS.TRACE_ID],
    messageType = this[MessageDlqs.MESSAGE_DLQS.MESSAGE_TYPE],
    errorMessage = this[MessageDlqs.MESSAGE_DLQS.ERROR_MESSAGE],
    retryCount = this[MessageDlqs.MESSAGE_DLQS.RETRY_COUNT]!!,
    status = DlqStatus.valueOf(this[MessageDlqs.MESSAGE_DLQS.STATUS]!!),
    lastAttemptedAt = this[MessageDlqs.MESSAGE_DLQS.LAST_ATTEMPTED_AT]?.toInstant(),
    createdAt = this[MessageDlqs.MESSAGE_DLQS.CREATED_AT]!!.toInstant(),
)
