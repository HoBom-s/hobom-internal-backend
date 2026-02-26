package com.hobom.hobominternal.domain.message.model

import com.hobom.hobominternal.config.DescribableEnum

enum class MessageType(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PUSH_MESSAGE("PUSH_MESSAGE", "Web Push Notification"),
    MAIL_MESSAGE("MAIL_MESSAGE", "Mail Push Message"),
}
