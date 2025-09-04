package com.hobom.hobominternal.domain.approval.model

import org.springframework.stereotype.Component

@JvmInline
value class ApprovalRequestId(private val value: Long) {
    init {
        require(value > 0) { "ApprovalRequestId must be a positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}

@Component
class TenantIdProvider {
    fun current(): ApprovalRequestTenantId = ApprovalRequestTenantId("hobom")
}
