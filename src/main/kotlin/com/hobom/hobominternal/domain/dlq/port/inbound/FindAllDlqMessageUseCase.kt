package com.hobom.hobominternal.domain.dlq.port.inbound

import com.hobom.hobominternal.adapter.outbound.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.shared.page.QueryResult

interface FindAllDlqMessageUseCase {
    fun invoke(page: Int, size: Int): QueryResult<DlqMessageQueryResult>
}
