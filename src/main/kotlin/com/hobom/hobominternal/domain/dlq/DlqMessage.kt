package com.hobom.hobominternal.domain.dlq

import java.time.Instant

data class DlqMessage(
    val id: Long,
    val topic: String,
    val partition: Int,
    val kafkaOffset: Long,
    val key: String?,
    val value: String,
    val traceId: String?,
    val messageType: String?,
    val errorMessage: String?,
    val retryCount: Int,
    val status: DlqStatus,
    val lastAttemptedAt: Instant?,
    val createdAt: Instant,
) {
    fun markAsSuccess(): DlqMessage =
        this.copy(status = DlqStatus.SUCCESS)

    fun markAsFailed(newError: String): DlqMessage =
        this.copy(
            retryCount = retryCount + 1,
            status = DlqStatus.FAILED,
            errorMessage = newError,
            lastAttemptedAt = Instant.now(),
        )

    fun markAsRetrying(): DlqMessage =
        this.copy(status = DlqStatus.RETRYING, lastAttemptedAt = Instant.now())
}
