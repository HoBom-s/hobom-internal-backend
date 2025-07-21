package com.hobom.hobominternal.domain.dlq.model

import java.time.Instant

data class DlqMessageCreateRequest(
    val topic: String,
    val partition: Int,
    val kafkaOffset: Long,
    val key: String?,
    val value: String,
    val traceId: String?,
    val messageType: String?,
    val errorMessage: String?,
    val status: DlqStatus = DlqStatus.PENDING,
    val retryCount: Int = 1,
    val lastAttemptedAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
)
