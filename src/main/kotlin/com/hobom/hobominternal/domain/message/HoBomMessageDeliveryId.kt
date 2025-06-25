package com.hobom.hobominternal.domain.message

@JvmInline
value class HoBomMessageDeliveryId(private val value: Long) {
    init {
        require(value > 0) { "HoBomMessageDeliveryId must be positive number" }
    }

    override fun toString(): String = value.toString()

    fun toRaw(): Long = value
}
