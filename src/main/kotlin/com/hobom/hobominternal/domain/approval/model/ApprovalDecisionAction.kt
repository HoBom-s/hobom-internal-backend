package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalDecisionAction(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    APPROVE("APPROVE", "approval-request-approve-action"),
    REJECT("REJECT", "approval-request-reject-action"),
}
