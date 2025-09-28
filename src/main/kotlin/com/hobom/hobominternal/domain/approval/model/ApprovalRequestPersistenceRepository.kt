package com.hobom.hobominternal.domain.approval.model

interface ApprovalRequestPersistenceRepository {
    fun load(id: ApprovalRequestId): ApprovalRequest

    fun save(approvalRequest: ApprovalRequest)
}
