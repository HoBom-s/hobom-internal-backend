package com.hobom.hobominternal.domain.log.model

data class EndpointErrorCount(
    val path: String,
    val httpMethod: String,
    val totalCount: Long,
    val errorCount: Long,
)
