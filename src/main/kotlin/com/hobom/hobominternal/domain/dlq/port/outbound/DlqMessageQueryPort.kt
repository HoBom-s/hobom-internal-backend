package com.hobom.hobominternal.domain.dlq.port.outbound

import com.hobom.hobominternal.adapter.outbound.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.shared.page.QueryResult

interface DlqMessageQueryPort {
    fun findById(id: DlqMessageId): DlqMessageQueryResult

    fun findAll(page: Int, size: Int): QueryResult<DlqMessageQueryResult>
}
