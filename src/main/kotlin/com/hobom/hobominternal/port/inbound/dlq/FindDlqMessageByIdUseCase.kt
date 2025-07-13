package com.hobom.hobominternal.port.inbound.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.DlqMessageId

interface FindDlqMessageByIdUseCase {
    fun invoke(id: DlqMessageId): DlqMessageQueryResult
}
