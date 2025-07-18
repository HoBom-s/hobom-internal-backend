package com.hobom.hobominternal.domain.log.model

@JvmInline
value class HoBomLogId(private val value: Long) {
    init {
        require(value > 0) { "HoBomLogId must be a positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}
