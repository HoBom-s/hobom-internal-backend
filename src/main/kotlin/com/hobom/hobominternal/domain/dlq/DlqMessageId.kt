package com.hobom.hobominternal.domain.dlq

@JvmInline
value class DlqMessageId(private val value: Long) {
    init {
        require(value > 0) { "DlqMessageId must be a positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}
