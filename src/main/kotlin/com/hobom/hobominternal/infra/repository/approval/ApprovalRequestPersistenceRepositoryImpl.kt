package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestPersistenceRepository
import org.jooq.DSLContext
import org.jooq.generated.tables.references.APPROVAL_REQUEST
import org.springframework.stereotype.Repository

@Repository
class ApprovalRequestPersistenceRepositoryImpl(
    private val dsl: DSLContext
) : ApprovalRequestPersistenceRepository {
    override fun save(approvalRequest: ApprovalRequest) {
        ApprovalRequestSqlMapper
            .toInsertMap(dsl.insertInto(APPROVAL_REQUEST), approvalRequest)
            .execute()
    }
}