package com.hobom.hobominternal.domain.message

interface HoBomMessageDeliveryPersistenceRepository {
    fun save(request: HoBomMessageDeliveryHistoryCreateRequest)
}