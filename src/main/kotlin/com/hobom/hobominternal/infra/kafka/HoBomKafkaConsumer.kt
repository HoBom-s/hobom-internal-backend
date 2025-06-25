package com.hobom.hobominternal.infra.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord

abstract class HoBomKafkaConsumer<T : Any>(
    private val objectMapper: ObjectMapper,
    private val handler: KafkaMessageHandler<T>,
    private val clazz: Class<T>,
) {
    open fun consume(record: ConsumerRecord<String, String>) {
        try {
            val message = objectMapper.readValue(record.value(), clazz)
            if (isDuplicated(message, record)) {
                return
            }

            handler.handle(message)
            acknowledgeProcessed(message, record)
        } catch (e: Exception) {
            // Message process failed.
            // Try to send `Dead Letter Queue`.
            // DefaultErrorHandler or fallback DLQ.
            throw e
        }
    }

    open fun isDuplicated(message: T, record: ConsumerRecord<String, String>): Boolean = false

    open fun acknowledgeProcessed(message: T, record: ConsumerRecord<String, String>) {}
}
