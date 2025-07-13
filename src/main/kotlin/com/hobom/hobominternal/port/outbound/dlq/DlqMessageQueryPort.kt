package com.hobom.hobominternal.port.outbound.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.shared.page.QueryResult

interface DlqMessageQueryPort {
    fun findById(id: DlqMessageId): DlqMessageQueryResult

    fun findAll(page: Int, size: Int): QueryResult<DlqMessageQueryResult>
}
