package com.hobom.hobominternal.infra.repository.log

import com.hobom.hobominternal.domain.log.model.HoBomLogCommandRepository
import org.jooq.DSLContext
import org.jooq.generated.tables.references.HOBOM_LOGS
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class HoBomLogCommandRepositoryImpl(
    private val dsl: DSLContext,
) : HoBomLogCommandRepository {
    override fun deleteOlderThan(threshold: LocalDateTime, limit: Int): Int {
        return dsl.deleteFrom(HOBOM_LOGS)
            .where(
                HOBOM_LOGS.ID.`in`(
                    dsl.select(HOBOM_LOGS.ID)
                        .from(HOBOM_LOGS)
                        .where(HOBOM_LOGS.CREATED_AT.lt(threshold))
                        .orderBy(HOBOM_LOGS.ID.asc())
                        .limit(limit),
                ),
            )
            .execute()
    }
}
