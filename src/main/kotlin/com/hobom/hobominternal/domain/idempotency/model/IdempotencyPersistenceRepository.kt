package com.hobom.hobominternal.domain.idempotency.model

interface IdempotencyPersistenceRepository {
    fun acquire(scope: String, key: String): IdempotencyAcquireStatus
}
