package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.LogStatusCount
import com.hobom.hobominternal.port.inbound.log.CountStatusHoBomLogUseCase
import com.hobom.hobominternal.port.outbound.log.CountStatusHoBomLogQueryPort
import org.springframework.stereotype.Service

@Service
class CountStatusHoBomLogService(
    private val countStatusHoBomLogQueryPort: CountStatusHoBomLogQueryPort,
) : CountStatusHoBomLogUseCase {
    override fun invoke(): List<LogStatusCount> {
        return countStatusHoBomLogQueryPort.count()
    }
}
