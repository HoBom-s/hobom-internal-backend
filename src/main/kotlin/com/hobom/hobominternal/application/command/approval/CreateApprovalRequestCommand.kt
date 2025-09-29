package com.hobom.hobominternal.application.command.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStatus

data class CreateApprovalRequestCommand(
    val title: String,
    val content: String,
    val requesterId: String,
) {
    fun toSaveModel(): ApprovalRequest {
        return ApprovalRequest(
            title = title,
            content = content,
            requesterId = requesterId,
            status = ApprovalRequestStatus.PENDING,
            currentStage = 1,
        )
    }
}
