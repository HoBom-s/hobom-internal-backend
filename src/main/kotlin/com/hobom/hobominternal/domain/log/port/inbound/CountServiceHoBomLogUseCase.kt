package com.hobom.hobominternal.domain.log.port.inbound

import com.hobom.hobominternal.domain.log.model.ServiceTypeCount

interface CountServiceHoBomLogUseCase {
    fun invoke(hours: Int): List<ServiceTypeCount>
}
