package com.hobom.hobominternal.application.service.dlq

import com.hobom.hobominternal.adapter.outbound.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.port.inbound.FindDlqMessageByIdUseCase
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessageQueryPort
import org.springframework.stereotype.Service

@Service
class FindDlqMessageByIdService(
    private val dlqMessageQueryPort: DlqMessageQueryPort,
) : FindDlqMessageByIdUseCase {
    override fun invoke(id: DlqMessageId): DlqMessageQueryResult {
        return findBy(id)
    }

    private fun findBy(id: DlqMessageId): DlqMessageQueryResult = dlqMessageQueryPort
        .findById(id)
}
