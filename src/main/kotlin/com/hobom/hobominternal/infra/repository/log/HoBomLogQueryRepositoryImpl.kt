package com.hobom.hobominternal.infra.repository.log

import com.example.jooq.generated.tables.HobomLogs.HOBOM_LOGS
import com.hobom.hobominternal.domain.log.HoBomLog
import com.hobom.hobominternal.domain.log.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.HoBomLogSearchCriteria
import com.hobom.hobominternal.domain.log.HoBomLogSqlMapper
import com.hobom.hobominternal.domain.log.toConditions
import com.hobom.hobominternal.shared.page.QueryResult
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

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
}
