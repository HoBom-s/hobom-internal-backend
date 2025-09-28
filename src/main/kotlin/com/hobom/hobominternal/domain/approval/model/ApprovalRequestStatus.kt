package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalRequestStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PENDING("PENDING", "approval-request-status-pending"),
    APPROVED("APPROVED", "approval-request-status-approved"),
    REJECTED("REJECTED", "approval-request-status-rejected"),
}
