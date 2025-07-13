package com.hobom.hobominternal.adapter.inbound.dto.dlq

import com.hobom.hobominternal.adapter.outbound.query.dlq.DlqMessageQueryResult
import com.hobom.hobominternal.domain.dlq.DlqStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "DLQ Message Response")
data class DlqMessageResponse(
    @Schema(description = "DLQ UUID")
    val id: Long,

    @Schema(description = "Kafka message topic")
    val topic: String,

    @Schema(description = "Kafka partition")
    val partition: Int,

    @Schema(description = "Kafka consumer offset")
    val kafkaOffset: Long,

    @Schema(description = "Kafka consumer record key")
    val key: String?,

    @Schema(description = "Body payload")
    val value: String,

    @Schema(description = "TraceId")
    val traceId: String?,

    @Schema(description = "Message command class name")
    val messageType: String?,

    @Schema(description = "Error message")
    val errorMessage: String?,

    @Schema(description = "Retry count")
    val retryCount: Int,

    @Schema(description = "Status")
    val status: DlqStatus,

    @Schema(description = "DLQ Retry last attempt at")
    val lastAttemptedAt: Instant?,

    @Schema(description = "CreatedAt")
    val createdAt: Instant,
) {
    companion object {
        fun from(query: DlqMessageQueryResult): DlqMessageResponse = DlqMessageResponse(
            id = query.id.toRaw(),
            topic = query.topic,
            partition = query.partition,
            kafkaOffset = query.kafkaOffset,
            key = query.key,
            value = query.value,
            traceId = query.traceId,
            messageType = query.messageType,
            errorMessage = query.errorMessage,
            retryCount = query.retryCount,
            status = query.status,
            lastAttemptedAt = query.lastAttemptedAt,
            createdAt = query.createdAt,
        )
    }
}
