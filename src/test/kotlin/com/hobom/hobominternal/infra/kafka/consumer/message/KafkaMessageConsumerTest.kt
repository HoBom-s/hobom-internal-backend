package com.hobom.hobominternal.infra.kafka.consumer.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hobom.hobominternal.adapter.inbound.kafka.message.DeliverHoBomMessageHandler
import com.hobom.hobominternal.domain.message.MessageType
import com.hobom.hobominternal.port.outbound.dlq.DlqMessagePersistencePort
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import java.time.Instant

class KafkaMessageConsumerTest {
    private val objectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule.Builder().build())
    private val handler = mockk<DeliverHoBomMessageHandler>(relaxed = true)
    private val port = mockk<DlqMessagePersistencePort>(relaxed = true)
    private val consumer = KafkaMessageConsumer(objectMapper, handler, port)

    @Test
    fun `handle should parse message and invoke use case`() {
        val json = """
            {
              "type": "PUSH_MESSAGE",
              "title": "Today's Menu",
              "body": "Sushi",
              "recipient": "1234",
              "senderId": "sender",
              "sentAt": "2025-06-25T13:30:00Z"
            }
        """.trimIndent()
        val record = ConsumerRecord<String, String>(KafkaTopics.HoBomMessages.TOPIC, 0, 0L, null, json)

        consumer.consume(record)

        verify {
            handler.handle(
                withArg { command ->
                    assert(command.type == MessageType.PUSH_MESSAGE)
                    assert(command.title == "Today's Menu")
                    assert(command.body == "Sushi")
                    assert(command.recipient == "1234")
                    assert(command.senderId == "sender")
                    assert(command.sentAt == Instant.parse("2025-06-25T13:30:00Z"))
                },
            )
        }
    }
}
