package com.hobom.hobominternal.adapter.outbound.message

import com.hobom.hobominternal.domain.message.model.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.model.HoBomMessageDeliveryHistoryPersistenceRepository
import com.hobom.hobominternal.domain.message.port.outbound.HoBomMessageDeliveryHistoryPersistencePort
import org.springframework.stereotype.Component

@Component
class HoBomMessageDeliveryHistoryPersistenceAdapter(
    private val hoBomMessageDeliveryHistoryPersistenceRepository: HoBomMessageDeliveryHistoryPersistenceRepository,
) : HoBomMessageDeliveryHistoryPersistencePort {
    override fun save(request: HoBomMessageDeliveryHistoryCreateRequest) {
        hoBomMessageDeliveryHistoryPersistenceRepository.save(request)
    }
}
