package com.hobom.hobominternal.domain.notification.model

data class NotificationRequest(
    val category: NotificationCategory,
    val recipient: String,
    val title: String,
    val body: String,
    val senderId: String?,
)
