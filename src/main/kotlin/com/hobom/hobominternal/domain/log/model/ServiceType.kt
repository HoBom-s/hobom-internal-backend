package com.hobom.hobominternal.domain.log.model

import com.hobom.hobominternal.config.DescribableEnum

enum class ServiceType(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    HOBOM_BACKEND("HOBOM_BACKEND", "hobom-backend-server"),
    HOBOM_API_GATEWAY("HOBOM_API_GATEWAY", "hobom-api-gateway"),
    HOBOM_MESSAGE_DELIVERY("HOBOM_MESSAGE_DELIVERY", "hobom-message-delivery"),
    ;

    override fun toString(): String = value
}
