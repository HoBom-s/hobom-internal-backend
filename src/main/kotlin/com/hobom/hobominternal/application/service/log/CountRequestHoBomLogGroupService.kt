package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.RequestCount
import com.hobom.hobominternal.port.inbound.log.CountRequestHoBomLogGroupUseCase
import com.hobom.hobominternal.port.outbound.log.CountRequestHoBomLogGroupQueryPort
import org.springframework.stereotype.Service

@Service
class CountRequestHoBomLogGroupService(
    private val countRequestHoBomLogQueryPort: CountRequestHoBomLogGroupQueryPort,
) : CountRequestHoBomLogGroupUseCase {
    override fun invoke(): List<RequestCount> {
        return countRequestHoBomLogQueryPort.count()
    }
}
