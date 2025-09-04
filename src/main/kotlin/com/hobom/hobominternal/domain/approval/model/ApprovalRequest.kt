package com.hobom.hobominternal.domain.approval.model

class ApprovalRequest private constructor(
    var id: ApprovalRequestId?,
    val tenantId: ApprovalRequestTenantId,
    val requesterId: String,
    var title: String,
    var status: ApprovalRequestStatus,
    var version: Int,
    val resource: ApprovalResource?,
    val contextSnapshot: ApprovalContextSnapshot,
    val contextHash: String?,
    val templateId: ApprovalRequestTemplateId?,
    val stages: List<ApprovalRequestStage>,
) {
    companion object {
        fun create(
            title: String,
            tenantId: ApprovalRequestTenantId,
            templateId: ApprovalRequestTemplateId,
            requesterId: String,
            resource: ApprovalResource,
            stages: List<ApprovalRequestStage>,
        ): ApprovalRequest = ApprovalRequest(
            id = null,
            title = title,
            tenantId = tenantId,
            requesterId = requesterId,
            status = ApprovalRequestStatus.DRAFT,
            version = 1,
            resource = resource,
            contextSnapshot = ApprovalContextSnapshot("""{"event":"approval-created"}"""),
            contextHash = null,
            templateId = templateId,
            stages = stages,
        )

        fun rehydrate(
            id: Long,
            tenantId: ApprovalRequestTenantId,
            requesterId: String,
            title: String,
            status: ApprovalRequestStatus,
            version: Int,
            resource: ApprovalResource?,
            contextSnapshot: String,
            contextHash: String?,
            templateId: ApprovalRequestTemplateId,
            stages: List<ApprovalRequestStage>,
        ): ApprovalRequest =
            ApprovalRequest(
                id = ApprovalRequestId(id),
                tenantId = tenantId,
                requesterId = requesterId,
                title = title,
                status = status, version = version, resource = resource,
                contextSnapshot = ApprovalContextSnapshot(contextSnapshot),
                contextHash = contextHash, templateId = templateId,
                stages = stages.toMutableList(),
            )
    }
}
