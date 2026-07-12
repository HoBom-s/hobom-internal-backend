package com.hobom.hobominternal.infra.feign.hobom.dto

data class CreateNotificationRequest(
    val category: String,
    val recipient: String,
    val title: String,
    val body: String,
    val senderId: String?,
)
