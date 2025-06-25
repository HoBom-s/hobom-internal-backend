package com.hobom.hobominternal.domain.message

import java.time.Instant

data class HoBomMessageDeliveryHistoryCreateRequest(
    val type: MessageType,
    val title: String,
    val body: String,
    val recipient: String,
    val senderId: String?,
    val sentAt: Instant,
)
