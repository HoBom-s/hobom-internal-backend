package com.hobom.hobominternal.application.command.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus

data class CreateApprovalStageRequestCommand(
    val approvalRequestId: ApprovalRequestId,
    val orderNo: Int,
    val status: ApprovalStageStatus,
    val requireCount: Int,
)
