package com.hobom.hobominternal.application.service.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.port.inbound.dlq.FindDlqMessageByIdUseCase
import com.hobom.hobominternal.port.outbound.dlq.DlqMessageQueryPort
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
