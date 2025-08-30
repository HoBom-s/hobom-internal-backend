package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalStageStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PENDING("PENDING", "stage-pending-status"),
    APPROVED("APPROVED", "stage-approved-status"),
    REJECTED("REJECTED", "stage-rejected-status"),
    SKIPPED("SKIPPED", "stage-skipped-status"),
}
