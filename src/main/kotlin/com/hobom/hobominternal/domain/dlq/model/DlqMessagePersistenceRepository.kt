package com.hobom.hobominternal.domain.dlq.model

interface DlqMessagePersistenceRepository {
    fun load(id: DlqMessageId): DlqMessage

    fun save(request: DlqMessageCreateRequest)

    fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest)
}
