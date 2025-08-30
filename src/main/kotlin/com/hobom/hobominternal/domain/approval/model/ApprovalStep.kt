package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

data class ApprovalStep(
    val id: ApprovalStepId? = null,
    val approvalId: ApprovalRequestId,
    val stageOrder: Long,
    val mode: ApprovalStepMode,
    val status: ApprovalStepStatus,
    val comment: String,
    val decidedBy: String,
    val decidedAt: Instant,
)