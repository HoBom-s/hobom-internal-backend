package com.hobom.hobominternal.infra.kafka.consumer.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.hobom.hobominternal.adapter.inbound.kafka.message.DeliverHoBomMessageHandler
import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.infra.kafka.HoBomKafkaConsumer
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaMessageConsumer(
    private val objectMapper: ObjectMapper,
    private val handler: DeliverHoBomMessageHandler,
) : HoBomKafkaConsumer<DeliverHoBomMessageCommand>(
    objectMapper,
    handler,
    DeliverHoBomMessageCommand::class.java,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomMessages.TOPIC],
        containerFactory = "messageKafkaListenerContainerFactory",
        autoStartup = "true"
    )
    override fun consume(record: ConsumerRecord<String, String>) {
        super.consume(record)
    }
}
