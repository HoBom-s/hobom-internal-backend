package com.hobom.hobominternal.domain.approval.port.outbound

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId

interface ApprovalRequestPersistencePort {
    fun load(id: ApprovalRequestId): ApprovalRequest

    fun saveNew(approvalRequest: ApprovalRequest)
}
