package com.hobom.hobominternal.adapter.outbound.dlq

import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqMessageQueryRepository
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessageQueryPort
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
