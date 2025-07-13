package com.hobom.hobominternal.port.inbound.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.shared.page.QueryResult

interface FindAllDlqMessageUseCase {
    fun invoke(page: Int, size: Int): QueryResult<DlqMessageQueryResult>
}
