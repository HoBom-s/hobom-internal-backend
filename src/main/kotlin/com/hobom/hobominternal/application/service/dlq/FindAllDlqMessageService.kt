package com.hobom.hobominternal.application.service.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.port.inbound.dlq.FindAllDlqMessageUseCase
import com.hobom.hobominternal.port.outbound.dlq.DlqMessageQueryPort
import com.hobom.hobominternal.shared.page.QueryResult
import org.springframework.stereotype.Service

@Service
class FindAllDlqMessageService(
    private val dlqMessageQueryPort: DlqMessageQueryPort,
) : FindAllDlqMessageUseCase {
    override fun invoke(page: Int, size: Int): QueryResult<DlqMessageQueryResult> {
        return findAll(page, size)
    }

    private fun findAll(page: Int, size: Int): QueryResult<DlqMessageQueryResult> = dlqMessageQueryPort
        .findAll(page, size)
}
