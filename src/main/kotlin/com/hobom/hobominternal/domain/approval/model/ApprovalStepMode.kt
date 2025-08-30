package com.hobom.hobominternal.domain.approval.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ApprovalStepMode(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    ALL("ALL", "all-passed"), // 모두가 승인 해야 하는 경우
    ANY("ANY", "any-passed"), // 한명만 승인해도 되는 경우
}
