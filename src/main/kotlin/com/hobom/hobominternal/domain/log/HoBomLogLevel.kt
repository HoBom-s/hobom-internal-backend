package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.config.DescribableEnum

enum class HoBomLogLevel(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    DEBUG("DEBUG", "debug-level"),
    INFO("INFO", "info-level"),
    WARN("WARN", "warn-level"),
    ERROR("ERROR", "error-level"),
    FATAL("FATAL", "fatal-level"),
    ;

    override fun toString(): String = value
}
