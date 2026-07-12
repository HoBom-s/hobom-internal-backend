package com.hobom.hobominternal.domain.log.port.outbound

import com.hobom.hobominternal.domain.log.model.ServiceTypeCount

interface CountServiceHoBomLogQueryPort {
    fun count(hours: Int): List<ServiceTypeCount>
}
