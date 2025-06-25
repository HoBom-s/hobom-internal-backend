package com.hobom.hobominternal.infra.kafka.consumer.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HttpMethodType
import com.hobom.hobominternal.domain.log.ServiceType
import com.hobom.hobominternal.port.inbound.log.SaveBulkLogUseCase
import com.hobom.hobominternal.shared.kafka.KafkaTopics
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KafkaLogConsumerTest {
    private val saveBulkLogUseCase = mockk<SaveBulkLogUseCase>(relaxed = true)
    private lateinit var consumer: KafkaLogConsumer

    @BeforeEach
    fun setup() {
        consumer = spyk(KafkaLogConsumer(saveBulkLogUseCase))
        every { consumer.flush() } just Runs
    }

    @Test
    fun `handle should trigger flush when buffer size reaches batch size`() {
        val payload = SaveLogCommand(
            serviceType = ServiceType.HOBOM_BACKEND,
            level = HoBomLogLevel.INFO,
            traceId = "trace-123",
            message = "Test log",
            httpMethod = HttpMethodType.POST,
            path = "/test",
            statusCode = 200,
            host = "hobom-internal",
            userId = "user-1",
            payload = mapOf("key" to "value"),
        )
        val json = jacksonObjectMapper().writeValueAsString(payload)
        val record = ConsumerRecord(KafkaTopics.HoBomLogs.TOPIC, 0, 0L, "key", json)

        repeat(100) {
            consumer.handle(record)
        }

        verify { consumer.flush() }
    }
}
