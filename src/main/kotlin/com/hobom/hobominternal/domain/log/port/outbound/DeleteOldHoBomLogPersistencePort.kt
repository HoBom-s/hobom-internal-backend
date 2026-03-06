package com.hobom.hobominternal.domain.log.port.outbound

import java.time.LocalDateTime

interface DeleteOldHoBomLogPersistencePort {
    fun deleteBatch(threshold: LocalDateTime, batchSize: Int): Int
}
