package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

class ApprovalAssignment(
    val id: ApprovalAssignmentId,
    val approvalStageId: ApprovalStageId,
    val approvalRequestId: ApprovalRequestId,
    val comment: String?,
    val decidedAt: Instant,
    val createdAt: Instant,
    val updated: Instant,
)