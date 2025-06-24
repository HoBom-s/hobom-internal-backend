package com.hobom.hobominternal.domain.log

import com.example.jooq.generated.tables.HobomLogs.HOBOM_LOGS
import org.jooq.Condition
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class HoBomLogSearchCriteria(
    val serviceType: ServiceType? = null,
    val httpMethod: HttpMethodType? = null,
    val statusCode: Int? = null,
    val startedAt: Instant? = null,
    val endedAt: Instant? = null,
)

fun HoBomLogSearchCriteria.toConditions(): List<Condition> = buildList {
    serviceType?.let {
        add(HOBOM_LOGS.SERVICE_TYPE.eq(it.name))
    }
    httpMethod?.let {
        add(HOBOM_LOGS.HTTP_METHOD.eq(it.name))
    }
    statusCode?.let {
        add(HOBOM_LOGS.STATUS_CODE.eq(it))
    }
    startedAt?.let {
        add(HOBOM_LOGS.TIMESTAMP.ge(LocalDateTime.ofInstant(it, ZoneId.systemDefault())))
    }
    endedAt?.let {
        add(HOBOM_LOGS.TIMESTAMP.le(LocalDateTime.ofInstant(it, ZoneId.systemDefault())))
    }
}
