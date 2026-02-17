package com.hobom.hobominternal.domain.approval.model

@JvmInline
value class ApprovalStageId(private val value: Long) {
    init {
        require(value > 0) { "ApprovalStageId must be a positive number" }
    }

    override fun toString(): String {
        return value.toString()
    }

    fun toRaw(): Long = value
}
