package com.hobom.hobominternal.domain.notification.model

import com.hobom.hobominternal.config.DescribableEnum

enum class NotificationCategory(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    SYSTEM("SYSTEM", "시스템"),
    TODO("TODO", "할 일"),
    MESSAGE("MESSAGE", "메시지"),
    NOTE("NOTE", "노트"),
}
