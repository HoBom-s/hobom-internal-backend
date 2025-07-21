package com.hobom.hobominternal.domain.notion.port.inbound

interface GetNotionBlockUseCase {
    fun invoke(id: String): String
}
