package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.hobom.hobominternal.domain.log.HoBomLogModel
import com.hobom.hobominternal.port.outbound.log.SaveHoBomLogPort
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class SaveHoBomLogAdapter(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : SaveHoBomLogPort {
    override fun saveAll(logs: List<HoBomLogModel>) {
        with(jdbcTemplate) {
            batchUpdate(createSqlSyntax(), HoBomLogSqlMapper.map(logs))
        }
    }

    private fun createSqlSyntax(): String = """
        INSERT INTO logs (
            service_type, level, trace_id, message, http_method,
            path, status_code, host, user_id, payload, timestamp, created_at, updated_at
        ) VALUES (
            :serviceType, :level, :traceId, :message, :httpMethod,
            :path, :statusCode, :host, :userId, :payload, :timestamp, now(), now()
        )
    """.trimIndent()
}
