package com.hobom.hobominternal.shared.kafka

object KafkaTopics {
    object HoBomLogs {
        const val TOPIC = "hobom.logs"
        const val GROUP = "log-consumer-group"
    }

    object HoBomMessages {
        const val TOPIC = "hobom.messages"
        const val GROUP = "message-consumer-group"
    }
}
