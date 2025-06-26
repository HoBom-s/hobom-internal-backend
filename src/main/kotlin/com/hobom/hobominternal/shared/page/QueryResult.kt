package com.hobom.hobominternal.shared.page

data class QueryResult<T>(
    val items: List<T>,
    val total: Long,
)
