package com.hobom.hobominternal.infra.kafka.consumer.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.hobom.hobominternal.adapter.inbound.kafka.log.SaveHoBomLogBatchHandler
import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.domain.log.model.HoBomLogLevel
import com.hobom.hobominternal.domain.log.model.HttpMethodType
import com.hobom.hobominternal.domain.log.model.ServiceType
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test

class KafkaLogConsumerTest {
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val handler = mockk<SaveHoBomLogBatchHandler>(relaxed = true)
    private val consumer = KafkaLogConsumer(objectMapper, handler)

    @Test
    fun `should buffer and flush SaveLogCommand`() {
        val command = SaveLogCommand(
            serviceType = ServiceType.HOBOM_BACKEND,
            level = HoBomLogLevel.INFO,
            traceId = "trace-123",
            message = "Test log message",
            httpMethod = HttpMethodType.GET,
            path = "/api/test",
            statusCode = 200,
            host = "localhost",
            userId = "user-1",
            payload = mapOf("key" to "value"),
        )
        val json = objectMapper.writeValueAsString(command)
        val record = ConsumerRecord(KafkaTopics.HoBomLogs.TOPIC, 0, 0L, "key", json)

        repeat(100) {
            consumer.consume(record)
        }

        verify(exactly = 1) {
            handler.handle(
                withArg {
                    assert(it.size == 100)
                    assert(it[0].traceId == "trace-123")
                },
            )
        }
    }
}
