package com.hobom.hobominternal.adapter.outbound.dlq

import com.hobom.hobominternal.domain.dlq.model.DlqMessage
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.model.DlqStatus
import java.time.Instant

data class DlqMessageQueryResult(
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
    companion object {
        fun from(domain: DlqMessage): DlqMessageQueryResult = DlqMessageQueryResult(
            id = domain.id,
            topic = domain.topic,
            partition = domain.partition,
            kafkaOffset = domain.kafkaOffset,
            key = domain.key,
            value = domain.value,
            traceId = domain.traceId,
            messageType = domain.messageType,
            errorMessage = domain.errorMessage,
            retryCount = domain.retryCount,
            status = domain.status,
            lastAttemptedAt = domain.lastAttemptedAt,
            createdAt = domain.createdAt,
        )
    }
}
