package com.hobom.hobominternal.infra.kafka.consumer.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hobom.hobominternal.application.command.message.SaveHoBomMessageDeliveryHistoryCommand
import com.hobom.hobominternal.port.inbound.message.SaveHoBomMessageDeliveryHistoryUseCase
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaMessageConsumer(
    private val saveHoBomMessageDeliveryHistoryUseCase: SaveHoBomMessageDeliveryHistoryUseCase,
    // For JavaTimeModule
    private val objectMapper: ObjectMapper,
) {
    @KafkaListener(
        topics = [KafkaTopics.HoBomMessages.TOPIC],
        groupId = KafkaTopics.HoBomMessages.GROUP,
    )
    open fun handle(record: ConsumerRecord<String, String>) {
        try {
            val command = objectMapper.readValue<SaveHoBomMessageDeliveryHistoryCommand>(record.value())
            saveHoBomMessageDeliveryHistoryUseCase.invoke(command)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
