package com.hobom.hobominternal.infra.kafka.consumer.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.hobom.hobominternal.adapter.inbound.kafka.message.DeliverHoBomMessageHandler
import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessagePersistencePort
import com.hobom.hobominternal.infra.kafka.HoBomKafkaConsumer
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class KafkaMessageConsumer(
    private val objectMapper: ObjectMapper,
    private val handler: DeliverHoBomMessageHandler,
    private val dlqMessagePersistencePort: DlqMessagePersistencePort,
) : HoBomKafkaConsumer<DeliverHoBomMessageCommand>(
    objectMapper,
    handler,
    DeliverHoBomMessageCommand::class.java,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomMessages.TOPIC],
        containerFactory = "messageKafkaListenerContainerFactory",
        autoStartup = "true",
    )
    override fun consume(record: ConsumerRecord<String, String>) {
        super.consume(record)
    }

    override fun onConsumeFailed(
        rawValue: String,
        exception: Exception,
        record: ConsumerRecord<String, String>,
    ) {
        // TODO: error handling
        dlqMessagePersistencePort.save(
            DlqMessageCreateRequest(
                topic = record.topic(),
                partition = record.partition(),
                kafkaOffset = record.offset(),
                key = record.key(),
                value = rawValue,
                traceId = extractTraceId(rawValue),
                messageType = DeliverHoBomMessageCommand::class.java.canonicalName,
                errorMessage = exception.message,
                lastAttemptedAt = Instant.now(),
            ),
        )
    }

    private fun extractTraceId(value: String): String? =
        runCatching { objectMapper.readTree(value).get("traceId")?.asText() }.getOrNull()
}
