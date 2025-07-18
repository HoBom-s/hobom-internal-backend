package com.hobom.hobominternal.domain.message.model

interface HoBomMessageDeliveryHistoryPersistenceRepository {
    fun save(request: HoBomMessageDeliveryHistoryCreateRequest)
}
