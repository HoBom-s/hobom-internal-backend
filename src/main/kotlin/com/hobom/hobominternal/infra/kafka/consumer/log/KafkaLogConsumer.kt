package com.hobom.hobominternal.infra.kafka.consumer.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.hobom.hobominternal.adapter.inbound.kafka.log.SaveHoBomLogBatchHandler
import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.infra.kafka.HoBomBufferedKafkaConsumer
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaLogConsumer(
    private val objectMapper: ObjectMapper,
    private val handler: SaveHoBomLogBatchHandler,
) : HoBomBufferedKafkaConsumer<SaveLogCommand>(
    objectMapper,
    handler,
    SaveLogCommand::class.java,
    batchSize = 100,
    flushDelayMillis = 5000L,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomLogs.TOPIC],
        containerFactory = "logKafkaListenerContainerFactory",
        autoStartup = "true"
    )
    override fun consume(record: ConsumerRecord<String, String>) {
        super.consume(record)
    }
}
