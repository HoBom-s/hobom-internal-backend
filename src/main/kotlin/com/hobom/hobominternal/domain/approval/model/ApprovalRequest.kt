package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

class ApprovalRequest(
    val id: ApprovalRequestId,
    val title: String,
    val content: String,
    val requesterId: String,
    val status: ApprovalRequestStatus,
    val currentStage: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)
