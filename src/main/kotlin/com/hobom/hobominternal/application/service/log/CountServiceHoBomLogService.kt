package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.model.ServiceTypeCount
import com.hobom.hobominternal.domain.log.port.inbound.CountServiceHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.CountServiceHoBomLogQueryPort
import org.springframework.stereotype.Service

@Service
class CountServiceHoBomLogService(
    private val countServiceHoBomLogQueryPort: CountServiceHoBomLogQueryPort,
) : CountServiceHoBomLogUseCase {
    override fun invoke(hours: Int): List<ServiceTypeCount> {
        return countServiceHoBomLogQueryPort.count(hours)
    }
}
