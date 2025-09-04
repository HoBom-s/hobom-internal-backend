package com.hobom.hobominternal.application.service.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStage
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTemplateIdGen
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTenantId
import com.hobom.hobominternal.domain.approval.model.ApprovalResource
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus
import com.hobom.hobominternal.domain.approval.model.ApprovalStep
import com.hobom.hobominternal.domain.approval.model.ApprovalStepStatus
import com.hobom.hobominternal.domain.approval.model.TenantIdProvider
import com.hobom.hobominternal.domain.approval.port.inbound.CreateApprovalRequestUseCase
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalRequestPersistencePort
import com.hobom.hobominternal.domain.idempotency.model.IdempotencyAcquireStatus
import com.hobom.hobominternal.domain.idempotency.port.outbound.IdempotencyAcquirePort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class CreateApprovalRequestService(
    private val approvalRequestPersistencePort: ApprovalRequestPersistencePort,
    private val idempotencyAcquirePort: IdempotencyAcquirePort,
    private val tenantIdProvider: TenantIdProvider,
) : CreateApprovalRequestUseCase {
    @Transactional
    override fun invoke(command: CreateApprovalRequestCommand, idempotencyKey: String) {
        command.validate()

        val scope = scopeForApprovalCreate()
        val acquiredIdempotencyStatus = idempotencyAcquirePort.acquire(scope, idempotencyKey)
        validateIdempotencyOfApprovalRequest(acquiredIdempotencyStatus)

        val tenantId = tenantIdProvider.current()
        val approvalRequest = command.toDomain(tenantId)
        approvalRequestPersistencePort.saveNew(approvalRequest)
    }

    private fun scopeForApprovalCreate(): String = "approval.create"

    private fun validateIdempotencyOfApprovalRequest(idempotencyStatus: IdempotencyAcquireStatus) {
        if (idempotencyStatus.isExisting()) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "request in-flight: try later")
        }
    }

    private fun CreateApprovalRequestCommand.validate() {
        val orders = stages.map { it.order }.sorted()
        require(orders.first() == 1) { "stages.order must start from 1" }
    }

    private fun CreateApprovalRequestCommand.toDomain(tenantId: ApprovalRequestTenantId): ApprovalRequest {
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
            tenantId = tenantId,
            templateId = createdTemplateId,
            requesterId = requesterId,
            resource = createdResource,
            stages = createdStages,
        )
    }
}
