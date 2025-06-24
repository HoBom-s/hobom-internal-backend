package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.example.jooq.generated.tables.records.HobomLogsRecord
import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HoBomLogSqlMapper
import com.hobom.hobominternal.domain.log.HttpMethodType
import com.hobom.hobominternal.domain.log.ServiceType
import org.assertj.core.api.Assertions.assertThat
import org.jooq.JSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class HoBomLogSqlMapperTest {
    @Test
    fun `should convert HoBomLogModel to SQL params correctly`() {
        val logs = listOf(
            HoBomLog(
                serviceType = ServiceType.HOBOM_BACKEND,
                level = HoBomLogLevel.INFO,
                traceId = "trace-123",
                message = "test",
                httpMethod = HttpMethodType.POST,
                path = "/api",
                statusCode = 200,
                host = "localhost",
                userId = "user-1",
                payload = mapOf("k" to "v"),
                timestamp = Instant.now(),
            ),
        )
        val sqlParams = HoBomLogSqlMapper.map(logs)

        assertThat(sqlParams).hasSize(1)
        assertThat(sqlParams[0].getValue("traceId")).isEqualTo("trace-123")
    }

    @Test
    fun `should map HobomLogsRecord to HoBomLog correctly`() {
        val record = HobomLogsRecord()
        val now = LocalDateTime.now()

        record.id = 1L
        record.serviceType = "HOBOM_BACKEND"
        record.level = "INFO"
        record.traceId = "trace-123"
        record.message = "test-message"
        record.httpMethod = "GET"
        record.path = "/some/path"
        record.statusCode = 200
        record.host = "localhost"
        record.userId = "user-1"
        record.payload = JSON.valueOf("""{"key":"value"}""")
        record.timestamp = now
        record.createdAt = now
        record.updatedAt = now

        val result = HoBomLogSqlMapper.fromRecord(record)
        val expectedInstant = now.atZone(ZoneId.systemDefault()).toInstant()

        assertEquals(ServiceType.HOBOM_BACKEND, result.serviceType)
        assertEquals(HoBomLogLevel.INFO, result.level)
        assertEquals("trace-123", result.traceId)
        assertEquals("test-message", result.message)
        assertEquals(HttpMethodType.GET, result.httpMethod)
        assertEquals("/some/path", result.path)
        assertEquals(200, result.statusCode)
        assertEquals("localhost", result.host)
        assertEquals("user-1", result.userId)
        assertEquals(mapOf("key" to "value"), result.payload)
        assertEquals(expectedInstant, result.timestamp)
    }
}
