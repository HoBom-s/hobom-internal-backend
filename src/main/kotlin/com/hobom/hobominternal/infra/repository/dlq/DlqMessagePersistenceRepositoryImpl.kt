package com.hobom.hobominternal.infra.repository.dlq

import com.example.jooq.generated.tables.MessageDlqs.MESSAGE_DLQS
import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqMessagePersistenceRepository
import com.hobom.hobominternal.exception.DlqMessageNotFoundException
import org.jooq.DSLContext
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

    override fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest) {
        DlqMessageSqlMapper.upsert(dsl, id.toRaw(), request)
    }
}
