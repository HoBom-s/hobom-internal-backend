package com.hobom.hobominternal.domain.approval.model

interface ApprovalRequestPersistenceRepository {
    fun save(approvalRequest: ApprovalRequest)
}