package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStatus
import org.jooq.InsertSetMoreStep
import org.jooq.InsertSetStep
import org.jooq.generated.tables.records.ApprovalRequestRecord
import org.jooq.generated.tables.references.APPROVAL_REQUEST
import java.time.Instant

object ApprovalRequestSqlMapper {
    fun toInsertMap(
        insert: InsertSetStep<ApprovalRequestRecord>,
        request: ApprovalRequest,
    ): InsertSetMoreStep<ApprovalRequestRecord> {
        return insert
            .set(APPROVAL_REQUEST.TITLE, request.title)
            .set(APPROVAL_REQUEST.CONTENT, request.content)
            .set(APPROVAL_REQUEST.REQUESTER_ID, request.requesterId)
            .set(APPROVAL_REQUEST.STATUS, request.status.value)
            .set(APPROVAL_REQUEST.CURRENT_STAGE, request.currentStage)
    }

    fun fromRecord(record: ApprovalRequestRecord): ApprovalRequest = ApprovalRequest(
        id = ApprovalRequestId(record.id ?: -1),
        title = record.title ?: "",
        content = record.content ?: "",
        requesterId = record.requesterId ?: "",
        status = ApprovalRequestStatus.valueOf(record.status ?: "PENDING"),
        currentStage = record.currentStage ?: 1,
        createdAt = record.createdAt?.toInstant() ?: Instant.now(),
        updatedAt = record.updatedAt?.toInstant() ?: Instant.now(),
    )
}
