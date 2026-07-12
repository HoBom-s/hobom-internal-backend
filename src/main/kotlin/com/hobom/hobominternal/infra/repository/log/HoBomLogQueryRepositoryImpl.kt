package com.hobom.hobominternal.infra.repository.log

import com.hobom.hobominternal.domain.log.model.EndpointErrorCount
import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.HoBomLogSearchCriteria
import com.hobom.hobominternal.domain.log.model.LogLevelCount
import com.hobom.hobominternal.domain.log.model.LogStatusCount
import com.hobom.hobominternal.domain.log.model.RequestCount
import com.hobom.hobominternal.domain.log.model.ServiceTypeCount
import com.hobom.hobominternal.domain.log.model.toConditions
import com.hobom.hobominternal.exception.HoBomLogNotFoundException
import com.hobom.hobominternal.shared.page.QueryResult
import org.jooq.DSLContext
import org.jooq.generated.tables.references.HOBOM_LOGS
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Repository
class HoBomLogQueryRepositoryImpl(
    private val dsl: DSLContext,
) : HoBomLogQueryRepository {
    override fun findFilteredLogs(
        criteria: HoBomLogSearchCriteria,
        page: Int,
        size: Int,
    ): QueryResult<HoBomLog> {
        val offset = ((page - 1).coerceAtLeast(0)) * size
        val conditions = criteria.toConditions()
        val total = dsl.selectCount()
            .from(HOBOM_LOGS)
            .where(conditions)
            .fetchOne(0, Long::class.java) ?: 0
        val records = dsl.selectFrom(HOBOM_LOGS)
            .where(conditions)
            .orderBy(HOBOM_LOGS.ID.desc())
            .limit(size)
            .offset(offset)
            .map { HoBomLogSqlMapper.fromRecord(it) }

        return QueryResult(
            items = records.map { it },
            total = total,
        )
    }

    override fun findById(id: HoBomLogId): HoBomLog {
        val record = dsl.selectFrom(HOBOM_LOGS)
            .where(HOBOM_LOGS.ID.eq(id.toRaw()))
            .fetchOne() ?: throw HoBomLogNotFoundException(id.toRaw())

        return HoBomLogSqlMapper.fromRecord(record)
    }

    override fun countStatusCode(hours: Int): List<LogStatusCount> {
        val since = LocalDateTime.now().minusHours(hours.toLong())

        return dsl.select(HOBOM_LOGS.STATUS_CODE, DSL.count())
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(since))
            .groupBy(HOBOM_LOGS.STATUS_CODE)
            .orderBy(HOBOM_LOGS.STATUS_CODE.asc())
            .fetch()
            .mapNotNull { row ->
                row[HOBOM_LOGS.STATUS_CODE]?.let { statusCode ->
                    LogStatusCount(statusCode = statusCode, count = row.get(DSL.count()))
                }
            }
    }

    override fun countRequestsGroupedByMinute(hours: Int): List<RequestCount> {
        val since = LocalDateTime.now().minusHours(hours.toLong())
        val minuteField = DSL.field(
            "date_trunc('minute', {0})",
            OffsetDateTime::class.java,
            HOBOM_LOGS.CREATED_AT,
        ).`as`("minute")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return dsl.select(minuteField, DSL.count().`as`("total_requests"))
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(since))
            .groupBy(minuteField)
            .orderBy(minuteField.desc())
            .fetch()
            .map {
                val minute = it.get("minute", OffsetDateTime::class.java) ?: OffsetDateTime.MIN
                val formattedMinute = minute.format(formatter)
                RequestCount(
                    minute = formattedMinute,
                    totalRequests = it.get("total_requests", Long::class.java) ?: 0L,
                )
            }
    }

    override fun countByLevel(hours: Int): List<LogLevelCount> {
        val since = LocalDateTime.now().minusHours(hours.toLong())

        return dsl.select(HOBOM_LOGS.LEVEL, DSL.count().cast(Long::class.java))
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(since))
            .groupBy(HOBOM_LOGS.LEVEL)
            .orderBy(DSL.count().desc())
            .fetch()
            .mapNotNull { row ->
                row[HOBOM_LOGS.LEVEL]?.let { level ->
                    LogLevelCount(
                        level = level,
                        count = row.get(DSL.count().cast(Long::class.java)) ?: 0L,
                    )
                }
            }
    }

    override fun countByServiceType(hours: Int): List<ServiceTypeCount> {
        val since = LocalDateTime.now().minusHours(hours.toLong())

        return dsl.select(HOBOM_LOGS.SERVICE_TYPE, DSL.count().cast(Long::class.java))
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(since))
            .groupBy(HOBOM_LOGS.SERVICE_TYPE)
            .orderBy(DSL.count().desc())
            .fetch()
            .mapNotNull { row ->
                row[HOBOM_LOGS.SERVICE_TYPE]?.let { serviceType ->
                    ServiceTypeCount(
                        serviceType = serviceType,
                        count = row.get(DSL.count().cast(Long::class.java)) ?: 0L,
                    )
                }
            }
    }

    override fun topErrorEndpoints(hours: Int, limit: Int): List<EndpointErrorCount> {
        val since = LocalDateTime.now().minusHours(hours.toLong())
        val totalCount = DSL.count().cast(Long::class.java).`as`("total_count")
        val errorCount = DSL.count().filterWhere(HOBOM_LOGS.STATUS_CODE.ge(400))
            .cast(Long::class.java).`as`("error_count")

        return dsl.select(HOBOM_LOGS.PATH, HOBOM_LOGS.HTTP_METHOD, totalCount, errorCount)
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(since))
            .and(HOBOM_LOGS.PATH.isNotNull)
            .groupBy(HOBOM_LOGS.PATH, HOBOM_LOGS.HTTP_METHOD)
            .orderBy(DSL.field("error_count").desc(), DSL.field("total_count").desc())
            .limit(limit)
            .fetch()
            .map { row ->
                EndpointErrorCount(
                    path = row[HOBOM_LOGS.PATH] ?: "",
                    httpMethod = row[HOBOM_LOGS.HTTP_METHOD] ?: "",
                    totalCount = row.get("total_count", Long::class.java) ?: 0L,
                    errorCount = row.get("error_count", Long::class.java) ?: 0L,
                )
            }
    }
}
