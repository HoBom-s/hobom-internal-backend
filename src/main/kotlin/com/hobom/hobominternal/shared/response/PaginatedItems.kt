package com.hobom.hobominternal.shared.response

data class PaginatedItems<T>(
    val items: List<T>,
    val totalCount: Long,
    val offset: Int,
    val limit: Int,
)
