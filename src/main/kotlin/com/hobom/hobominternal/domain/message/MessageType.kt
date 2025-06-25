package com.hobom.hobominternal.domain.message

import com.hobom.hobominternal.config.DescribableEnum

enum class MessageType(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    PUSH_MESSAGE("PUSH_MESSAGE", "FCM Push Message"),
    MAIL_MESSAGE("MAIL_MESSAGE", "Mail Push Message"),
}
