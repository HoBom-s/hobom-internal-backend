package com.hobom.hobominternal.domain.dlq

interface DlqMessagePersistenceRepository {
    fun load(id: DlqMessageId): DlqMessage

    fun save(request: DlqMessageCreateRequest)
}
