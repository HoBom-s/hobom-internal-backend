package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

class ApprovalRequestStage(
    var id: ApprovalStageId? = null,
    val order: Int,
    var mode: ApprovalStepMode,
    var approvalStageStatus: ApprovalStageStatus,
    var decidedBy: String? = null,
    var decidedAt: Instant? = null,
    var comment: String? = null,
    val approvalRequestSteps: List<ApprovalStep> = mutableListOf(),
)
