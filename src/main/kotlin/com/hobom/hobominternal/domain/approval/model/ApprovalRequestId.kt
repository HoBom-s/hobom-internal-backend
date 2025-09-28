package com.hobom.hobominternal.domain.approval.model

@JvmInline
value class ApprovalRequestId(private val value: Long) {
    init {
        require(value > 0) { "ApprovalRequestId must be a positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}