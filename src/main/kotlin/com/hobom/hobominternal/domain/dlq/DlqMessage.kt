package com.hobom.hobominternal.domain.dlq

import java.time.Instant

data class DlqMessage(
    val id: DlqMessageId,
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
        this.copy(
            status = DlqStatus.SUCCESS,
            retryCount = retryCount + 1,
            errorMessage = "",
            lastAttemptedAt = Instant.now(),
        )

    fun markAsFailed(newError: String): DlqMessage =
        this.copy(
            status = DlqStatus.FAILED,
            retryCount = retryCount + 1,
            errorMessage = newError,
            lastAttemptedAt = Instant.now(),
        )

    fun markAsRetrying(): DlqMessage =
        this.copy(
            status = DlqStatus.RETRYING,
            lastAttemptedAt = Instant.now(),
            errorMessage = "",
        )

    fun toCreateRequest(): DlqMessageCreateRequest = DlqMessageCreateRequest(
        topic = topic,
        partition = partition,
        kafkaOffset = kafkaOffset,
        key = key,
        value = value,
        traceId = traceId,
        messageType = messageType,
        errorMessage = errorMessage,
        status = status,
        retryCount = retryCount,
        lastAttemptedAt = lastAttemptedAt,
    )
}
