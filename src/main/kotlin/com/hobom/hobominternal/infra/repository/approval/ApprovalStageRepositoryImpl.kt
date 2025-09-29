package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalStage
import com.hobom.hobominternal.domain.approval.model.ApprovalStageId
import com.hobom.hobominternal.domain.approval.model.ApprovalStagePersistenceRepository
import com.hobom.hobominternal.exception.ApprovalException
import org.jooq.DSLContext
import org.jooq.generated.tables.references.APPROVAL_STAGE
import org.springframework.stereotype.Repository

@Repository
class ApprovalStageRepositoryImpl(
    private val dsl: DSLContext,
) : ApprovalStagePersistenceRepository {
    override fun load(id: ApprovalStageId): ApprovalStage {
        val approvalStageRecord = dsl.selectFrom(APPROVAL_STAGE)
            .where(APPROVAL_STAGE.ID.eq(id.toRaw()))
            .fetchOne() ?: throw ApprovalException.NotFoundException()

        return ApprovalStageSqlMapper.fromRecord(approvalStageRecord)
    }

    override fun save(approvalStage: ApprovalStage) {
        ApprovalStageSqlMapper
            .toInsertMap(dsl.insertInto(APPROVAL_STAGE), approvalStage)
            .execute()
    }
}
