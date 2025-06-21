package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HoBomLogModel
import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class HoBomLogSqlMapperTest {
    @Test
    fun `should convert HoBomLogModel to SQL params correctly`() {
        val logs = listOf(
            HoBomLogModel(
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
}
