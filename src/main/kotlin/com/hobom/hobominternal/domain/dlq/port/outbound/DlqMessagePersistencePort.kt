package com.hobom.hobominternal.domain.dlq.port.outbound

import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId

interface DlqMessagePersistencePort {
    fun load(id: DlqMessageId): DlqMessage

    fun save(request: DlqMessageCreateRequest)

    fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest)
}
