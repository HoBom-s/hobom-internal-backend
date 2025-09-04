package com.hobom.hobominternal.domain.idempotency.port.outbound

import com.hobom.hobominternal.domain.idempotency.model.IdempotencyAcquireStatus

interface IdempotencyAcquirePort {
    fun acquire(scope: String, key: String): IdempotencyAcquireStatus
}
