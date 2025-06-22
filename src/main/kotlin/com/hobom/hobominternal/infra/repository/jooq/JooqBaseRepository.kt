package com.hobom.hobominternal.infra.repository.jooq

import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.Table
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

abstract class JooqBaseRepository<T : Any, R : Any>(
    protected val dsl: DSLContext,
    private val table: Table<*>,
    private val rowType: Class<R>,
    private val rowToDomain: (R) -> T,
) {
    protected fun fetchPageBy(
        conditions: List<Condition>,
        pageable: Pageable,
        orderBy: List<SortField<*>> = listOf(),
    ): Page<T> {
        val whereClause = if (conditions.isEmpty()) DSL.trueCondition() else DSL.and(conditions)
        val rows = dsl.selectFrom(table)
            .where(whereClause)
            .orderBy(orderBy)
            .limit(pageable.pageSize)
            .offset(pageable.offset)
            .fetchInto(rowType)
        val domainObjects = rows.map(rowToDomain)
        val total = dsl.fetchCount(DSL.selectOne().from(table).where(whereClause))

        return PageImpl(domainObjects, pageable, total.toLong())
    }
}
