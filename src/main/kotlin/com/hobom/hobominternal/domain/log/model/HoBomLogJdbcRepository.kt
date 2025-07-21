package com.hobom.hobominternal.domain.log.model

interface HoBomLogJdbcRepository {
    fun batchInsert(logs: List<HoBomLog>)
}
