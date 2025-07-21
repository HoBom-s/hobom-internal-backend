package com.hobom.hobominternal.adapter.outbound.dlq

import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqMessagePersistenceRepository
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessagePersistencePort
import org.springframework.stereotype.Component

@Component
class DlqMessagePersistenceAdapter(
    private val dlqMessagePersistenceRepository: DlqMessagePersistenceRepository,
) : DlqMessagePersistencePort {
    override fun load(id: DlqMessageId): DlqMessage {
        return dlqMessagePersistenceRepository.load(id)
    }

    override fun save(request: DlqMessageCreateRequest) {
        dlqMessagePersistenceRepository.save(request)
    }

    override fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest) {
        dlqMessagePersistenceRepository.upsert(id, request)
    }
}
