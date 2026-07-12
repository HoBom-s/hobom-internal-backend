package com.hobom.hobominternal.infra.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import java.util.Collections

abstract class HoBomBufferedKafkaConsumer<T : Any>(
    private val objectMapper: ObjectMapper,
    private val handler: KafkaBatchMessageHandler<T>,
    private val clazz: Class<T>,
    private val batchSize: Int = 3,
    private val flushDelayMillis: Long = 5000L,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val buffer: MutableList<T> = Collections.synchronizedList(mutableListOf())

    open fun consume(record: ConsumerRecord<String, String>) {
        try {
            val messages: List<T> = objectMapper.readValue(
                record.value(),
                objectMapper.typeFactory.constructCollectionType(List::class.java, clazz),
            )
            buffer.addAll(messages)
            flushIfNeeded()
        } catch (e: Exception) {
            log.error("Failed to consume record topic={} partition={} offset={}", record.topic(), record.partition(), record.offset(), e)
            throw e
        }
    }

    private fun flushIfNeeded() {
        if (buffer.size >= batchSize) {
            flush()
        }
    }

    @Scheduled(fixedDelayString = "\${kafka.flush-delay:5000}")
    fun flush() {
        val snapshot = synchronized(buffer) {
            if (buffer.isEmpty()) return
            buffer.toList().also { buffer.clear() }
        }
        try {
            handler.handle(snapshot)
        } catch (e: Exception) {
            log.error("Failed to flush {} messages to handler", snapshot.size, e)
        }
    }
}
