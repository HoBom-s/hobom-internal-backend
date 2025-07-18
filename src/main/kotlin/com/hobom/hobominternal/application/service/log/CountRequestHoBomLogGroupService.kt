package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.model.RequestCount
import com.hobom.hobominternal.domain.log.port.inbound.CountRequestHoBomLogGroupUseCase
import com.hobom.hobominternal.domain.log.port.outbound.CountRequestHoBomLogGroupQueryPort
import org.springframework.stereotype.Service

@Service
class CountRequestHoBomLogGroupService(
    private val countRequestHoBomLogQueryPort: CountRequestHoBomLogGroupQueryPort,
) : CountRequestHoBomLogGroupUseCase {
    override fun invoke(): List<RequestCount> {
        return countRequestHoBomLogQueryPort.count()
    }
}
