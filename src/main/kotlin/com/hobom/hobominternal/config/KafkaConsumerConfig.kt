package com.hobom.hobominternal.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties.AckMode
import org.springframework.scheduling.annotation.EnableScheduling

@EnableKafka
@Configuration
@EnableScheduling
class KafkaConsumerConfig(
    private val kafkaProperties: KafkaProperties,
) {

    private fun createConsumerFactory(groupId: String): ConsumerFactory<String, String> {
        val props = kafkaProperties.buildConsumerProperties().toMutableMap()
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean(name = ["logKafkaListenerContainerFactory"])
    fun logKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = createConsumerFactory("log-consumer-group")
        factory.containerProperties.ackMode = AckMode.BATCH
        return factory
    }

    @Bean(name = ["messageKafkaListenerContainerFactory"])
    fun messageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = createConsumerFactory("message-consumer-group")
        factory.containerProperties.ackMode = AckMode.BATCH
        return factory
    }
}
