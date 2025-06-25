package com.hobom.hobominternal.infra.kafka.consumer.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hobom.hobominternal.application.command.log.SaveLogCommand
import com.hobom.hobominternal.port.inbound.log.SaveBulkLogUseCase
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Collections

@Component
class KafkaLogConsumer(
    private val saveBulkLogUseCase: SaveBulkLogUseCase,
) {
    companion object {
        private const val BATCH_SIZE = 100
        private const val FLUSH_DELAY_MS = 5000L
    }

    private val objectMapper = jacksonObjectMapper()
    private val buffer: MutableList<SaveLogCommand> = Collections.synchronizedList(mutableListOf())

    @KafkaListener(
        topics = ["hobom.logs"],
        groupId = "log-consumer-group",
        autoStartup = "true",
    )
    open fun handle(record: ConsumerRecord<String, String>) {
        try {
            val payload = objectMapper.readValue<SaveLogCommand>(record.value())
            addBuffer(payload)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        executeFlush()
    }

    @Scheduled(fixedDelay = FLUSH_DELAY_MS)
    fun flush() {
        if (buffer.isEmpty()) {
            return
        }

        saveBulkLogUseCase.invoke(buffer.toCommand())
        buffer.clear()
    }

    private fun addBuffer(payload: SaveLogCommand) {
        this.buffer += payload
    }

    private fun executeFlush() {
        if (buffer.size >= BATCH_SIZE) {
            flush()
        }
    }
}

fun MutableList<SaveLogCommand>.toCommand(): List<SaveLogCommand> = this.map {
    SaveLogCommand(
        serviceType = it.serviceType,
        level = it.level,
        traceId = it.traceId,
        message = it.message,
        httpMethod = it.httpMethod,
        path = it.path,
        statusCode = it.statusCode,
        host = it.host,
        userId = it.userId,
        payload = it.payload,
    )
}
