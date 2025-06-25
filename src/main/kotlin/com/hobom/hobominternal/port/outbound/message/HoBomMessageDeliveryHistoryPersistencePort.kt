package com.hobom.hobominternal.port.outbound.message

import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest

interface HoBomMessageDeliveryHistoryPersistencePort {
    fun save(request: HoBomMessageDeliveryHistoryCreateRequest)
}
