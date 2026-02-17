package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

class ApprovalStage(
    val id: ApprovalStageId? = null,
    val approvalRequestId: ApprovalRequestId,
    val orderNo: Int,
    val status: ApprovalStageStatus,
    val requiredCount: Int,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
