package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalStageStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    REVIEWING("REVIEWING", "approval-stage-status-reviewing"),
    DONE("DONE", "approval-stage-status-done"),
}
