package com.hobom.hobominternal.infra.kafka

interface KafkaMessageHandler<T> {
    fun handle(message: T)
}
