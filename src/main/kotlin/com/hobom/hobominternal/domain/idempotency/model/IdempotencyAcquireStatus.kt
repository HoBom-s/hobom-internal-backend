package com.hobom.hobominternal.domain.idempotency.model

import com.hobom.hobominternal.config.DescribableEnum

enum class IdempotencyAcquireStatus(
    override val value: String,
    override val description: String,
) : DescribableEnum {
    ACQUIRED("ACQUIRED", "Idempotency acquired"),
    EXISTING("EXISTING", "Idempotency existing"),
    ;

    fun isExisting(): Boolean = this == IdempotencyAcquireStatus.EXISTING
}
