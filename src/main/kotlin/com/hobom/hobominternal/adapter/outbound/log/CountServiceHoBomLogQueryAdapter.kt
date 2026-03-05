package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLogQueryRepository
import com.hobom.hobominternal.domain.log.model.ServiceTypeCount
import com.hobom.hobominternal.domain.log.port.outbound.CountServiceHoBomLogQueryPort
import org.springframework.stereotype.Component

@Component
class CountServiceHoBomLogQueryAdapter(
    private val hobomLogQueryRepository: HoBomLogQueryRepository,
) : CountServiceHoBomLogQueryPort {
    override fun count(hours: Int): List<ServiceTypeCount> {
        return hobomLogQueryRepository.countByServiceType(hours)
    }
}
