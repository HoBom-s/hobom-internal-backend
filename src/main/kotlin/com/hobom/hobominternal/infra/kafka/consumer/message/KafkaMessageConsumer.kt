package com.hobom.hobominternal.infra.kafka.consumer.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.hobom.hobominternal.adapter.inbound.kafka.message.SaveHoBomMessageHandler
import com.hobom.hobominternal.application.command.message.SaveHoBomMessageDeliveryHistoryCommand
import com.hobom.hobominternal.infra.kafka.HoBomKafkaConsumer
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaMessageConsumer(
    private val objectMapper: ObjectMapper,
    private val handler: SaveHoBomMessageHandler,
) : HoBomKafkaConsumer<SaveHoBomMessageDeliveryHistoryCommand>(
    objectMapper,
    handler,
    SaveHoBomMessageDeliveryHistoryCommand::class.java,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomMessages.TOPIC],
        groupId = KafkaTopics.HoBomMessages.GROUP,
    )
    override fun consume(record: ConsumerRecord<String, String>) {
        super.consume(record)
    }
}
