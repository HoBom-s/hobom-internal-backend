package com.hobom.hobominternal.infra.repository.log

import com.example.jooq.generated.enums.LogsHttpMethod
import com.example.jooq.generated.enums.LogsServiceType
import com.example.jooq.generated.tables.Logs.LOGS
import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogRow
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.infra.repository.jooq.JooqBaseRepository
import org.jooq.DSLContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneId

@Repository
class HoBomLogJooqRepository(
    dsl: DSLContext,
) : JooqBaseRepository<HoBomLog, HoBomLogRow>(
    dsl = dsl,
    table = LOGS,
    rowType = HoBomLogRow::class.java,
    rowToDomain = { it.toDomain() },
) {
    fun findFilteredLogs(criteria: HoBomLogSearchCriteria, pageable: Pageable): Page<HoBomLog> {
        val conditions = buildList {
            criteria.serviceType?.let {
                add(LOGS.SERVICE_TYPE.eq(LogsServiceType.valueOf(it.name)))
            }
            criteria.httpMethod?.let {
                add(LOGS.HTTP_METHOD.eq(LogsHttpMethod.valueOf(it.name)))
            }
            criteria.statusCode?.let {
                add(LOGS.STATUS_CODE.eq(it))
            }
            criteria.startedAt?.let {
                add(LOGS.TIMESTAMP.ge(LocalDateTime.ofInstant(it, ZoneId.systemDefault())))
            }
            criteria.endedAt?.let {
                add(LOGS.TIMESTAMP.le(LocalDateTime.ofInstant(it, ZoneId.systemDefault())))
            }
        }

        return fetchPageBy(
            conditions = conditions,
            pageable = pageable,
            orderBy = listOf(LOGS.ID.desc()),
        )
    }
}
