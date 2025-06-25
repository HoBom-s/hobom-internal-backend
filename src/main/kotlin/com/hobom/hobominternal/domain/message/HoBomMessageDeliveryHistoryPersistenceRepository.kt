package com.hobom.hobominternal.domain.message

interface HoBomMessageDeliveryHistoryPersistenceRepository {
    fun save(request: HoBomMessageDeliveryHistoryCreateRequest)
}
