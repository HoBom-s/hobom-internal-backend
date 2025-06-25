package com.hobom.hobominternal.adapter.outbound.persistence.message

import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryPersistenceRepository
import com.hobom.hobominternal.port.outbound.message.HoBomMessageDeliveryHistoryPersistencePort
import org.springframework.stereotype.Component

@Component
class HoBomMessageDeliveryHistoryPersistenceAdapter(
    private val hoBomMessageDeliveryHistoryPersistenceRepository: HoBomMessageDeliveryHistoryPersistenceRepository,
) : HoBomMessageDeliveryHistoryPersistencePort {
    override fun save(request: HoBomMessageDeliveryHistoryCreateRequest) {
        hoBomMessageDeliveryHistoryPersistenceRepository.save(request)
    }
}
