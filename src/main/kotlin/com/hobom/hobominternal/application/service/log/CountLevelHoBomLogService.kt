package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.model.LogLevelCount
import com.hobom.hobominternal.domain.log.port.inbound.CountLevelHoBomLogUseCase
import com.hobom.hobominternal.domain.log.port.outbound.CountLevelHoBomLogQueryPort
import org.springframework.stereotype.Service

@Service
class CountLevelHoBomLogService(
    private val countLevelHoBomLogQueryPort: CountLevelHoBomLogQueryPort,
) : CountLevelHoBomLogUseCase {
    override fun invoke(hours: Int): List<LogLevelCount> {
        return countLevelHoBomLogQueryPort.count(hours)
    }
}
