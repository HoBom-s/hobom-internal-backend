package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalStepStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PENDING("PENDING", "step-pending-status"),
    APPROVED("APPROVED", "step-approved-status"),
    REJECTED("REJECTED", "step-rejected-status"),
    SKIPPED("SKIPPED", "step-skipped-status"),
}
