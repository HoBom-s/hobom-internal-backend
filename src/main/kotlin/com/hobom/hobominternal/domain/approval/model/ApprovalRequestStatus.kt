package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalRequestStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    DRAFT("DRAFT", "approval-request-draft-status"),
    PENDING("PENDING", "approval-request-pending-status"),
    IN_REVIEW("IN_REVIEW", "approval-request-in-review-status"),
    APPROVED("APPROVED", "approval-request-approved-status"),
    REJECTED("REJECTED", "approval-request-rejected-status"),
    CANCELED("CANCELED", "approval-request-canceled-status"),
    EXPIRED("EXPIRED", "approval-request-expired-status"),
}
