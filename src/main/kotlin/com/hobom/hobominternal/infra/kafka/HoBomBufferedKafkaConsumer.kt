package com.hobom.hobominternal.infra.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.scheduling.annotation.Scheduled
import java.util.Collections

abstract class HoBomBufferedKafkaConsumer<T : Any>(
    private val objectMapper: ObjectMapper,
    private val handler: KafkaBatchMessageHandler<T>,
    private val clazz: Class<T>,
    private val batchSize: Int = 3,
    private val flushDelayMillis: Long = 5000L,
) {
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
            e.printStackTrace()
        }
    }

    private fun flushIfNeeded() {
        if (buffer.size >= batchSize) {
            flush()
        }
    }

    @Scheduled(fixedDelayString = "\${kafka.flush-delay:5000}")
    fun flush() {
        if (buffer.isEmpty()) {
            return
        }
        println("Buffer : ${buffer.toList()}")

        handler.handle(buffer.toList())
        buffer.clear()
    }
}
