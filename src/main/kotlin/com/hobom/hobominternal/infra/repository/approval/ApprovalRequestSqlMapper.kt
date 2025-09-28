package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import org.jooq.InsertSetStep
import org.jooq.Query
import org.jooq.generated.tables.records.ApprovalRequestRecord
import org.jooq.generated.tables.references.APPROVAL_REQUEST

object ApprovalRequestSqlMapper {
    fun toInsertMap(
        insert: InsertSetStep<ApprovalRequestRecord>,
        request: ApprovalRequest
    ): Query {
        return insert
            .set(APPROVAL_REQUEST.ID, request.id.toRaw())
            .set(APPROVAL_REQUEST.TITLE, request.title)
            .set(APPROVAL_REQUEST.CONTENT, request.content)
            .set(APPROVAL_REQUEST.REQUESTER_ID, request.requesterId)
            .set(APPROVAL_REQUEST.STATUS, request.status.value)
            .set(APPROVAL_REQUEST.CURRENT_STAGE, request.currentStage)
    }
}