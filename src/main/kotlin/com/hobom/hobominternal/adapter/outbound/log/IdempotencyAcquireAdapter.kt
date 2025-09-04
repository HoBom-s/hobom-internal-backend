package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.idempotency.model.IdempotencyAcquireStatus
import com.hobom.hobominternal.domain.idempotency.model.IdempotencyPersistenceRepository
import com.hobom.hobominternal.domain.idempotency.port.outbound.IdempotencyAcquirePort
import org.springframework.stereotype.Component

@Component
class IdempotencyAcquireAdapter(
    private val idempotencyPersistenceRepository: IdempotencyPersistenceRepository,
) : IdempotencyAcquirePort {
    override fun acquire(scope: String, key: String): IdempotencyAcquireStatus {
        return idempotencyPersistenceRepository.acquire(scope, key)
    }
}
