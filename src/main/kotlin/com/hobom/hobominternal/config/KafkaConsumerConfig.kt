package com.hobom.hobominternal.config

import com.hobom.hobominternal.domain.dlq.model.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.port.outbound.DlqMessagePersistencePort
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConsumerRecordRecoverer
import org.springframework.kafka.listener.ContainerProperties.AckMode
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.util.backoff.FixedBackOff

@EnableKafka
@Configuration
@EnableScheduling
class KafkaConsumerConfig(
    private val kafkaProperties: KafkaProperties,
    private val dlqMessagePersistencePort: DlqMessagePersistencePort,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean(name = ["logKafkaListenerContainerFactory"])
    fun logKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = createConsumerFactory("log-consumer-group")
        factory.containerProperties.ackMode = AckMode.BATCH
        factory.setCommonErrorHandler(createErrorHandler(::saveLogRecordToDlq))
        return factory
    }

    @Bean(name = ["messageKafkaListenerContainerFactory"])
    fun messageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = createConsumerFactory("message-consumer-group")
        factory.containerProperties.ackMode = AckMode.BATCH
        // DLQ 저장은 KafkaMessageConsumer.onConsumeFailed 에서 처리됨 — 여기서는 로그만 기록
        factory.setCommonErrorHandler(
            createErrorHandler { record, exception ->
                log.error(
                    "Message consumer exhausted retries: topic={} partition={} offset={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    exception,
                )
            },
        )
        return factory
    }

    @Bean(name = ["spaceEventKafkaListenerContainerFactory"])
    fun spaceEventKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = createConsumerFactory("space-event-consumer-group")
        factory.containerProperties.ackMode = AckMode.BATCH
        factory.setCommonErrorHandler(
            createErrorHandler { record, exception ->
                log.error(
                    "Space event consumer exhausted retries: topic={} partition={} offset={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    exception,
                )
            },
        )
        return factory
    }

    private fun createConsumerFactory(groupId: String): ConsumerFactory<String, String> {
        val props = kafkaProperties.buildConsumerProperties().toMutableMap()
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
        return DefaultKafkaConsumerFactory(props)
    }

    private fun createErrorHandler(recoverer: ConsumerRecordRecoverer): DefaultErrorHandler =
        DefaultErrorHandler(recoverer, FixedBackOff(1000L, 2)).apply {
            isAckAfterHandle = true
        }

    private fun saveLogRecordToDlq(record: org.apache.kafka.clients.consumer.ConsumerRecord<*, *>, exception: Exception) {
        log.error(
            "Log consumer exhausted retries — saving to DLQ: topic={} partition={} offset={}",
            record.topic(),
            record.partition(),
            record.offset(),
            exception,
        )
        runCatching {
            dlqMessagePersistencePort.save(
                DlqMessageCreateRequest(
                    topic = record.topic(),
                    partition = record.partition(),
                    kafkaOffset = record.offset(),
                    key = record.key()?.toString(),
                    value = record.value()?.toString() ?: "",
                    traceId = null,
                    messageType = null,
                    errorMessage = exception.message,
                ),
            )
        }.onFailure { e ->
            log.error("Failed to persist DLQ record: topic={} partition={} offset={}", record.topic(), record.partition(), record.offset(), e)
        }
    }
}
