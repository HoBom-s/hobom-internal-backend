package com.hobom.hobominternal.infra.repository.message

import com.example.jooq.generated.Tables.MESSAGE_DELIVERY_HISTORIES
import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryCreateRequest
import com.hobom.hobominternal.domain.message.HoBomMessageDeliveryHistoryPersistenceRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class HoBomMessageDeliveryHistoryPersistenceRepositoryImpl(
    private val dsl: DSLContext,
) : HoBomMessageDeliveryHistoryPersistenceRepository {
    override fun save(request: HoBomMessageDeliveryHistoryCreateRequest) {
        val now = Instant.now()
        HoBomMessageDeliveryHistorySqlMapper
            .toInsertMap(dsl.insertInto(MESSAGE_DELIVERY_HISTORIES), request, now)
            .execute()
    }
}
