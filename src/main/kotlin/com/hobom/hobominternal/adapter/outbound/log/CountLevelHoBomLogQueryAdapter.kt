package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.LogLevelCount
import com.hobom.hobominternal.domain.log.port.outbound.CountLevelHoBomLogQueryPort
import org.springframework.stereotype.Component

@Component
class CountLevelHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountLevelHoBomLogQueryPort {
    override fun count(hours: Int): List<LogLevelCount> {
        return hobomLogQueryRepository.countByLevel(hours)
    }
}
