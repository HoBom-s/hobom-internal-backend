package com.hobom.hobominternal.adapter.outbound.persistence.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hobom.hobominternal.domain.log.HoBomLogModel
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

object HoBomLogSqlMapper {
    private val objectMapper = jacksonObjectMapper()

    fun map(logs: List<HoBomLogModel>): Array<MapSqlParameterSource> = logs.map {
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
            .addValue("payload", it.payload?.let { p -> objectMapper.writeValueAsString(p) })
            .addValue("timestamp", it.timestamp)
    }.toTypedArray()
}
