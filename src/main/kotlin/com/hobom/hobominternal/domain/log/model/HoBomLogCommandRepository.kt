package com.hobom.hobominternal.domain.log.model

import java.time.LocalDateTime

interface HoBomLogCommandRepository {
    fun deleteOlderThan(threshold: LocalDateTime, limit: Int): Int
}
