package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalStage
import com.hobom.hobominternal.domain.approval.model.ApprovalStageId
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus
import org.jooq.InsertSetStep
import org.jooq.Query
import org.jooq.generated.tables.records.ApprovalStageRecord
import org.jooq.generated.tables.references.APPROVAL_STAGE
import java.time.Instant

object ApprovalStageSqlMapper {
    fun toInsertMap(
        insert: InsertSetStep<ApprovalStageRecord>,
        request: ApprovalStage,
    ): Query {
        return insert
            .set(APPROVAL_STAGE.APPROVAL_REQUEST_ID, request.approvalRequestId.toRaw())
            .set(APPROVAL_STAGE.ORDER_NO, request.orderNo)
            .set(APPROVAL_STAGE.STATUS, request.status.value)
            .set(APPROVAL_STAGE.REQUIRED_COUNT, request.requiredCount)
    }

    fun fromRecord(record: ApprovalStageRecord): ApprovalStage = ApprovalStage(
        id = ApprovalStageId(record.id ?: -1),
        approvalRequestId = ApprovalRequestId(record.approvalRequestId ?: -1),
        orderNo = record.orderNo ?: 1,
        status = ApprovalStageStatus.valueOf(record.status ?: "REVIEWING"),
        requiredCount = record.requiredCount ?: 1,
        createdAt = record.createdAt?.toInstant() ?: Instant.now(),
        updatedAt = record.updatedAt?.toInstant() ?: Instant.now(),
    )
}
