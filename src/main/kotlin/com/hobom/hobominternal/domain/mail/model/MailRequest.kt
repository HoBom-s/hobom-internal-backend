package com.hobom.hobominternal.domain.mail.model

data class MailRequest(
    val to: String,
    val title: String,
    val content: String,
)
