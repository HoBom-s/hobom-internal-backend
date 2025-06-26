package com.hobom.hobominternal.infra.kafka

interface KafkaBatchMessageHandler<T> {
    fun handle(messages: List<T>)
}
