package com.hobom.hobominternal.infra.repository.log

import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogJdbcRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class HoBomLogJdbcRepositoryImpl(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : HoBomLogJdbcRepository {
    override fun batchInsert(logs: List<HoBomLog>) {
        jdbcTemplate.batchUpdate(createInsertSql(), HoBomLogSqlMapper.map(logs))
    }

    private fun createInsertSql(): String = """
        INSERT INTO bear.hobom_logs (
            service_type, level, trace_id, message, http_method,
            path, status_code, host, user_id, payload, timestamp, created_at, updated_at
        ) VALUES (
            :serviceType, :level, :traceId, :message, :httpMethod,
            :path, :statusCode, :host, :userId, :payload, :timestamp, now(), now()
        )
    """.trimIndent()
}
