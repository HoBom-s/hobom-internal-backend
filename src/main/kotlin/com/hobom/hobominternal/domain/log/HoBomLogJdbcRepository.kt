package com.hobom.hobominternal.domain.log

interface HoBomLogJdbcRepository {
    fun batchInsert(logs: List<HoBomLog>)
}
