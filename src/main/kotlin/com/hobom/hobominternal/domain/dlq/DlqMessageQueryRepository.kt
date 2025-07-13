package com.hobom.hobominternal.domain.dlq

import com.hobom.hobominternal.shared.page.QueryResult

interface DlqMessageQueryRepository {
    fun findById(id: DlqMessageId): DlqMessage

    fun findAll(page: Int, size: Int): QueryResult<DlqMessage>
}
