package com.hobom.hobominternal.domain.message.port.outbound

import com.hobom.hobominternal.domain.message.model.HoBomMessageDeliveryHistoryCreateRequest

interface HoBomMessageDeliveryHistoryPersistencePort {
    fun save(request: HoBomMessageDeliveryHistoryCreateRequest)
}
