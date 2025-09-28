package com.hobom.hobominternal.domain.approval.model

@JvmInline
value class ApprovalAssignmentId(private val value: Long) {
    init {
        require(value > 0) { "ApprovalAssignmentId must be a positive number" }
    }

    override fun toString(): String {
        return value.toString()
    }

    fun toRaw(): Long = value
}