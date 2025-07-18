package com.hobom.hobominternal.domain.dlq.port.inbound

import com.hobom.hobominternal.adapter.outbound.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId

interface FindDlqMessageByIdUseCase {
    fun invoke(id: DlqMessageId): DlqMessageQueryResult
}
