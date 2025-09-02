package com.hobom.hobominternal.domain.approval.model

data class ApprovalResource(
    val type: String,
    val id: String,
    val version: Int? = 1,
)
