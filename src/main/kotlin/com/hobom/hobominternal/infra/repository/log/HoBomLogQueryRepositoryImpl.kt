package com.hobom.hobominternal.infra.repository.log

import com.example.jooq.generated.tables.HobomLogs.HOBOM_LOGS
import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.HoBomLogSearchCriteria
import com.hobom.hobominternal.domain.log.model.LogStatusCount
import com.hobom.hobominternal.domain.log.model.RequestCount
import com.hobom.hobominternal.domain.log.model.toConditions
import com.hobom.hobominternal.exception.HoBomLogNotFoundException
import com.hobom.hobominternal.shared.page.QueryResult
import org.jooq.DSLContext
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

    override fun countStatusCode(): List<LogStatusCount> {
        val oneHourAgo = LocalDateTime.now().minusHours(24)

        return dsl.select(HOBOM_LOGS.STATUS_CODE, DSL.count())
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(oneHourAgo))
            .groupBy(HOBOM_LOGS.STATUS_CODE)
            .orderBy(HOBOM_LOGS.STATUS_CODE.asc())
            .fetch()
            .map { LogStatusCount(statusCode = it[HOBOM_LOGS.STATUS_CODE], count = it.get(DSL.count())) }
    }

    override fun countRequestsGroupedByMinute(): List<RequestCount> {
        val oneHourAgo = LocalDateTime.now().minusHours(24)
        val minuteField = DSL.field("date_trunc('minute', {0})", OffsetDateTime::class.java, HOBOM_LOGS.CREATED_AT).`as`("minute")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return dsl.select(minuteField, DSL.count().`as`("total_requests"))
            .from(HOBOM_LOGS)
            .where(HOBOM_LOGS.CREATED_AT.gt(oneHourAgo))
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
}
