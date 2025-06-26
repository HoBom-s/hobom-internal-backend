package com.hobom.hobominternal.domain.mail

data class MailRequest(
    val to: String,
    val title: String,
    val content: String,
)
