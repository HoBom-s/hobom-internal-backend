package com.hobom.hobominternal.infra.repository.dlq

import com.example.jooq.generated.tables.MessageDlqs.MESSAGE_DLQS
import com.hobom.hobominternal.domain.dlq.DlqMessage
import com.hobom.hobominternal.domain.dlq.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.dlq.DlqMessagePersistenceRepository
import com.hobom.hobominternal.domain.dlq.DlqStatus
import com.hobom.hobominternal.exception.DlqMessageNotFoundException
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class DlqMessagePersistenceRepositoryImpl(
    private val dsl: DSLContext,
) : DlqMessagePersistenceRepository {
    override fun load(id: DlqMessageId): DlqMessage {
        val foundDlqMessage = dsl.selectFrom(MESSAGE_DLQS)
            .where(MESSAGE_DLQS.ID.eq(id.toRaw()))
            .fetchOne() ?: throw DlqMessageNotFoundException(id.toRaw())

        return foundDlqMessage.toDomain()
    }

    override fun save(request: DlqMessageCreateRequest) {
        DlqMessageSqlMapper
            .toInsertMap(dsl.insertInto(MESSAGE_DLQS), request)
            .execute()
    }

    private fun Record.toDomain(): DlqMessage = DlqMessage(
        id = this[MESSAGE_DLQS.ID]!!,
        topic = this[MESSAGE_DLQS.TOPIC]!!,
        partition = this[MESSAGE_DLQS.PARTITION]!!,
        kafkaOffset = this[MESSAGE_DLQS.KAFKA_OFFSET]!!,
        key = this[MESSAGE_DLQS.KEY],
        value = this[MESSAGE_DLQS.VALUE]!!.toString(),
        traceId = this[MESSAGE_DLQS.TRACE_ID],
        messageType = this[MESSAGE_DLQS.MESSAGE_TYPE],
        errorMessage = this[MESSAGE_DLQS.ERROR_MESSAGE],
        retryCount = this[MESSAGE_DLQS.RETRY_COUNT]!!,
        status = DlqStatus.valueOf(this[MESSAGE_DLQS.STATUS]!!),
        lastAttemptedAt = this[MESSAGE_DLQS.LAST_ATTEMPTED_AT].toInstant(),
        createdAt = this[MESSAGE_DLQS.CREATED_AT]!!.toInstant(),
    )
}
