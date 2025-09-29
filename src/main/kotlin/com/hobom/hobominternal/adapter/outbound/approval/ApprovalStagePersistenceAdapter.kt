package com.hobom.hobominternal.adapter.outbound.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalStageRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalStage
import com.hobom.hobominternal.domain.approval.model.ApprovalStageId
import com.hobom.hobominternal.domain.approval.model.ApprovalStagePersistenceRepository
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalStagePersistencePort
import org.springframework.stereotype.Component

@Component
class ApprovalStagePersistenceAdapter(
    private val approvalStagePersistenceRepository: ApprovalStagePersistenceRepository,
) : ApprovalStagePersistencePort {
    override fun load(id: ApprovalStageId): ApprovalStage {
        return approvalStagePersistenceRepository.load(id)
    }

    override fun save(command: CreateApprovalStageRequestCommand) {
        val approvalStage = toDomain(command)
        approvalStagePersistenceRepository.save(approvalStage)
    }

    private fun toDomain(command: CreateApprovalStageRequestCommand): ApprovalStage = ApprovalStage(
        approvalRequestId = command.approvalRequestId,
        status = command.status,
        orderNo = command.orderNo,
        requiredCount = command.requireCount,
    )
}
