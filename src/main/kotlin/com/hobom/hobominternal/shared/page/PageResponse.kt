package com.hobom.hobominternal.shared.page

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val total: Long,
)
