package com.hobom.hobominternal.infra.repository.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.model.HoBomLogLevel
import com.hobom.hobominternal.domain.log.model.HttpMethodType
import com.hobom.hobominternal.domain.log.model.ServiceType
import com.hobom.hobominternal.shared.json.JsonUtil
import org.jooq.generated.tables.records.HobomLogsRecord
import org.jooq.generated.tables.references.HOBOM_LOGS
import org.postgresql.util.PGobject
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.sql.Timestamp
import java.time.ZoneId

object HoBomLogSqlMapper {
    private val objectMapper = jacksonObjectMapper()

    fun map(logs: List<HoBomLog>): Array<MapSqlParameterSource> = logs.map {
        val json = it.payload?.let { payloadMap ->
            PGobject().apply {
                type = "jsonb"
                value = objectMapper.writeValueAsString(payloadMap)
            }
        }

        MapSqlParameterSource()
            .addValue("serviceType", it.serviceType.name)
            .addValue("level", it.level.name)
            .addValue("traceId", it.traceId)
            .addValue("message", it.message)
            .addValue("httpMethod", it.httpMethod.name)
            .addValue("path", it.path)
            .addValue("statusCode", it.statusCode)
            .addValue("host", it.host)
            .addValue("userId", it.userId)
            .addValue("payload", json)
            .addValue("timestamp", Timestamp.from(it.timestamp))
    }.toTypedArray()

    fun fromRecord(record: HobomLogsRecord): HoBomLog {
        val id = record[HOBOM_LOGS.ID]
        return HoBomLog(
            id = id?.let { HoBomLogId(it) },
            serviceType = ServiceType.valueOf(record[HOBOM_LOGS.SERVICE_TYPE].toString()),
            level = HoBomLogLevel.valueOf(record[HOBOM_LOGS.LEVEL].toString()),
            traceId = record[HOBOM_LOGS.TRACE_ID] ?: "",
            message = record[HOBOM_LOGS.MESSAGE] ?: "",
            httpMethod = HttpMethodType.valueOf(record[HOBOM_LOGS.HTTP_METHOD].toString()),
            path = record[HOBOM_LOGS.PATH],
            statusCode = record[HOBOM_LOGS.STATUS_CODE] ?: 200,
            host = record[HOBOM_LOGS.HOST] ?: "",
            userId = record[HOBOM_LOGS.USER_ID] ?: "",
            payload = record[HOBOM_LOGS.PAYLOAD]?.let { JsonUtil.parseJson(it.toString()) },
            timestamp = record[HOBOM_LOGS.TIMESTAMP]?.atZone(ZoneId.systemDefault())?.toInstant()
                ?: error("hobom_logs.timestamp is null for id=$id"),
            createdAt = record[HOBOM_LOGS.CREATED_AT]?.atZone(ZoneId.systemDefault())?.toInstant()
                ?: error("hobom_logs.created_at is null for id=$id"),
            updatedAt = record[HOBOM_LOGS.UPDATED_AT]?.atZone(ZoneId.systemDefault())?.toInstant()
                ?: error("hobom_logs.updated_at is null for id=$id"),
        )
    }
}
