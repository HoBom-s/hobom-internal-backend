package com.hobom.hobominternal.adapter.outbound.query.dlq

import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.dlq.DlqMessageQueryRepository
import com.hobom.hobominternal.port.outbound.dlq.DlqMessageQueryPort
import com.hobom.hobominternal.shared.page.QueryResult
import org.springframework.stereotype.Component

@Component
class DlqMessageQueryAdapter(
    private val dlqMessageQueryRepository: DlqMessageQueryRepository,
) : DlqMessageQueryPort {
    override fun findById(id: DlqMessageId): DlqMessageQueryResult {
        val dlq = dlqMessageQueryRepository.findById(id)

        return DlqMessageQueryResult.from(dlq)
    }

    override fun findAll(page: Int, size: Int): QueryResult<DlqMessageQueryResult> {
        val dlqQueryResult = dlqMessageQueryRepository.findAll(page, size)
        val items = dlqQueryResult.items.map { DlqMessageQueryResult.from(it) }

        return QueryResult(
            items = items,
            total = dlqQueryResult.total,
        )
    }
}
