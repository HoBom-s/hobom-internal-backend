package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.model.LogStatusCount
import com.hobom.hobominternal.domain.log.port.inbound.CountStatusHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.CountStatusHoBomLogQueryPort
import org.springframework.stereotype.Service

@Service
class CountStatusHoBomLogService(
    private val countStatusHoBomLogQueryPort: CountStatusHoBomLogQueryPort,
) : CountStatusHoBomLogUseCase {
    override fun invoke(): List<LogStatusCount> {
        return countStatusHoBomLogQueryPort.count()
    }
}
