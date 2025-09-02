package com.hobom.hobominternal.application.service.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStage
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTemplateIdGen
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTenantIdGen
import com.hobom.hobominternal.domain.approval.model.ApprovalResource
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus
import com.hobom.hobominternal.domain.approval.model.ApprovalStep
import com.hobom.hobominternal.domain.approval.model.ApprovalStepStatus
import com.hobom.hobominternal.domain.approval.port.inbound.CreateApprovalRequestUseCase
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalRequestPersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateApprovalRequestService(
    private val approvalRequestPersistencePort: ApprovalRequestPersistencePort,
) : CreateApprovalRequestUseCase {
    @Transactional
    override fun invoke(command: CreateApprovalRequestCommand) {
        command.validate()
        val approvalRequest = command.toDomain()
        approvalRequestPersistencePort.saveNew(approvalRequest)
    }

    private fun CreateApprovalRequestCommand.validate() {
        val orders = stages.map { it.order }.sorted()
        require(orders.first() == 1) { "stages.order must start from 1" }
    }

    private fun CreateApprovalRequestCommand.toDomain(): ApprovalRequest {
        val createdTenantId = ApprovalRequestTenantIdGen.slugRand(ApprovalRequestTenantIdGen.TENANT_DISPLAY_NAME)
        val createdTemplateId = ApprovalRequestTemplateIdGen.slugRand(ApprovalRequestTemplateIdGen.TEMPLATE_DISPLAY_NAME)
        val createdResource = ApprovalResource(resource.type, resource.id, resource.version)
        val createdStages = stages.map {
            ApprovalRequestStage(
                order = it.order,
                mode = it.mode,
                approvalStageStatus = ApprovalStageStatus.PENDING,
                approvalRequestSteps = it.steps.map { step ->
                    ApprovalStep(
                        approverIds = step.approverIds.toSet(),
                        status = ApprovalStepStatus.PENDING,
                    )
                },
            )
        }

        return ApprovalRequest.create(
            title = title,
            tenantId = createdTenantId,
            templateId = createdTemplateId,
            requesterId = requesterId,
            resource = createdResource,
            stages = createdStages,
        )
    }
}
