package com.hobom.hobominternal.port.inbound.notion

interface GetNotionBlockUseCase {
    fun invoke(id: String): String
}
