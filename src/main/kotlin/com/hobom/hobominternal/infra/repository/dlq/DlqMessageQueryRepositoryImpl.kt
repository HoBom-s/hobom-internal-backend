package com.hobom.hobominternal.infra.repository.dlq

import com.example.jooq.generated.tables.MessageDlqs.MESSAGE_DLQS
import com.hobom.hobominternal.domain.dlq.DlqMessage
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.dlq.DlqMessageQueryRepository
import com.hobom.hobominternal.exception.DlqMessageNotFoundException
import com.hobom.hobominternal.shared.page.QueryResult
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class DlqMessageQueryRepositoryImpl(
    private val dsl: DSLContext,
) : DlqMessageQueryRepository {
    override fun findById(id: DlqMessageId): DlqMessage {
        val record = dsl.selectFrom(MESSAGE_DLQS)
            .where(MESSAGE_DLQS.ID.eq(id.toRaw()))
            .fetchOne() ?: throw DlqMessageNotFoundException(id.toRaw())

        return record.toDomain()
    }

    override fun findAll(page: Int, size: Int): QueryResult<DlqMessage> {
        val offset = ((page - 1).coerceAtLeast(0)) * size
        val total = dsl.selectCount()
            .from(MESSAGE_DLQS)
            .fetchOne(0, Long::class.java) ?: 0
        val records = dsl.selectFrom(MESSAGE_DLQS)
            .orderBy(MESSAGE_DLQS.ID.desc())
            .limit(size)
            .offset(offset)
            .map { it.toDomain() }

        return QueryResult(
            items = records.map { it },
            total = total,
        )
    }
}
