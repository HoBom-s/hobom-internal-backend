package com.hobom.hobominternal.domain.approval.model

@JvmInline
value class ApprovalStepId(private val value: Long) {
    init {
        require(value > 0) { "ApprovalStepId must be a positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}
