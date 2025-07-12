package com.hobom.hobominternal.adapter.outbound.persistence.dlq

import com.hobom.hobominternal.domain.dlq.DlqMessage
import com.hobom.hobominternal.domain.dlq.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.dlq.DlqMessagePersistenceRepository
import com.hobom.hobominternal.port.outbound.dlq.DlqMessagePersistencePort
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
}
