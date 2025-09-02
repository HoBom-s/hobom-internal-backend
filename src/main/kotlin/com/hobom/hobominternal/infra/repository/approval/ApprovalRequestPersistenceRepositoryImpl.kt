package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestPersistenceRepository
import com.hobom.hobominternal.exception.ApprovalException
import org.jooq.DSLContext
import org.jooq.generated.tables.references.APPROVAL_REQUEST
import org.springframework.stereotype.Repository

@Repository
class ApprovalRequestPersistenceRepositoryImpl(
    private val dsl: DSLContext,
) : ApprovalRequestPersistenceRepository {
    override fun load(id: ApprovalRequestId): ApprovalRequest {
        val approvalRequest = dsl.selectFrom(APPROVAL_REQUEST)
            .where(APPROVAL_REQUEST.ID.eq(id.toRaw()))
            .fetchOne() ?: throw ApprovalException.NotFoundException()

        return ApprovalRequestMapper.rehydrateAggregate(dsl, approvalRequest)
    }

    override fun saveNew(approvalRequest: ApprovalRequest) {
        ApprovalRequestMapper.insertAggregate(dsl, approvalRequest)
    }
}
