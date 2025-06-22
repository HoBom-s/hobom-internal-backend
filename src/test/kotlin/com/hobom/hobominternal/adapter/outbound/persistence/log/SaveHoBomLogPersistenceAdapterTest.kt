package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.hobom.hobominternal.domain.log.HoBomLogLevel
import com.hobom.hobominternal.domain.log.HoBomLogModel
import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class SaveHoBomLogPersistenceAdapterTest {
    private val jdbcTemplate: NamedParameterJdbcTemplate = mockk(relaxed = true)
    private val adapter = SaveHoBomLogPersistenceAdapter(jdbcTemplate)

    @Test
    fun `should call jdbcTemplate batchUpdate in SaveHoBomLogAdapter`() {
        val logs = listOf(
            HoBomLogModel(
                serviceType = ServiceType.HOBOM_BACKEND,
                level = HoBomLogLevel.INFO,
                traceId = "abc-123",
                message = "Test Message",
                httpMethod = HttpMethodType.POST,
                path = "/test",
                statusCode = 200,
                host = "localhost",
                userId = "user-1",
                payload = mapOf("key" to "value"),
                timestamp = Instant.now(),
            ),
        )

        adapter.saveAll(logs)

        verify(exactly = 1) {
            jdbcTemplate.batchUpdate(any(), any<Array<MapSqlParameterSource>>())
        }
    }
}
