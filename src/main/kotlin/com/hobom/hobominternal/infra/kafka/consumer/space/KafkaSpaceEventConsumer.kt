package com.hobom.hobominternal.infra.kafka.consumer.space

import com.fasterxml.jackson.databind.ObjectMapper
import com.hobom.hobominternal.adapter.inbound.kafka.space.HandleSpaceEventHandler
import com.hobom.hobominternal.application.command.space.ProcessSpaceEventCommand
import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessagePersistencePort
import com.hobom.hobominternal.infra.kafka.HoBomKafkaConsumer
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class KafkaSpaceEventConsumer(
    private val objectMapper: ObjectMapper,
    private val handler: HandleSpaceEventHandler,
    private val dlqMessagePersistencePort: DlqMessagePersistencePort,
) : HoBomKafkaConsumer<ProcessSpaceEventCommand>(
    objectMapper,
    handler,
    ProcessSpaceEventCommand::class.java,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomSpaceEvents.TOPIC],
        containerFactory = "spaceEventKafkaListenerContainerFactory",
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
        dlqMessagePersistencePort.save(
            DlqMessageCreateRequest(
                topic = record.topic(),
                partition = record.partition(),
                kafkaOffset = record.offset(),
                key = record.key(),
                value = rawValue,
                traceId = null,
                messageType = ProcessSpaceEventCommand::class.java.canonicalName,
                errorMessage = exception.message,
                lastAttemptedAt = Instant.now(),
            ),
        )
    }
}
