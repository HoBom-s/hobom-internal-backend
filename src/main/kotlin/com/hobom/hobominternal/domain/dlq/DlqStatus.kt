package com.hobom.hobominternal.domain.dlq

import com.hobom.hobominternal.config.DescribableEnum

enum class DlqStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PENDING("PENDING", "status-pending"),
    RETRYING("RETRYING", "status-retrying"),
    SUCCESS("SUCCESS", "status-success"),
    FAILED("FAILED", "status-failed"),
}
