package com.hobom.hobominternal.domain.message

import java.time.Instant

data class HoBomMessageDeliveryHistory(
    val id: Long,
    val type: MessageType,
    val title: String,
    val body: String,
    val recipient: String,
    val senderId: String?,
    val sentAt: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
)
