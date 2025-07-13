package com.hobom.hobominternal.port.outbound.dlq

import com.hobom.hobominternal.domain.dlq.DlqMessage
import com.hobom.hobominternal.domain.dlq.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.DlqMessageId

interface DlqMessagePersistencePort {
    fun load(id: DlqMessageId): DlqMessage

    fun save(request: DlqMessageCreateRequest)

    fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest)
}
