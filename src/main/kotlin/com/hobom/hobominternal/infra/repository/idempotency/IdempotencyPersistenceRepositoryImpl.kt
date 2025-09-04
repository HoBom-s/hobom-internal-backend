package com.hobom.hobominternal.infra.repository.idempotency

import com.hobom.hobominternal.domain.idempotency.model.IdempotencyAcquireStatus
import com.hobom.hobominternal.domain.idempotency.model.IdempotencyPersistenceRepository
import org.jooq.DSLContext
import org.jooq.generated.tables.references.IDEMPOTENCY_KEYS
import org.springframework.stereotype.Repository

@Repository
class IdempotencyPersistenceRepositoryImpl(
    private val dsl: DSLContext,
) : IdempotencyPersistenceRepository {
    override fun acquire(scope: String, key: String): IdempotencyAcquireStatus {
        val inserted = dsl.insertInto(IDEMPOTENCY_KEYS)
            .set(IDEMPOTENCY_KEYS.SCOPE, scope)
            .set(IDEMPOTENCY_KEYS.KEY, key)
            .onConflict(IDEMPOTENCY_KEYS.SCOPE, IDEMPOTENCY_KEYS.KEY)
            .doNothing()
            .returning()
            .fetchOptional()

        return if (inserted.isPresent) {
            IdempotencyAcquireStatus.ACQUIRED
        } else {
            IdempotencyAcquireStatus.EXISTING
        }
    }
}
