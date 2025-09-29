package com.hobom.hobominternal.application.service.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import com.hobom.hobominternal.application.command.approval.CreateApprovalStageRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus
import com.hobom.hobominternal.domain.approval.port.inbound.CreateApprovalRequestUseCase
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalRequestPersistencePort
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalStagePersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateApprovalRequestService(
    private val approvalRequestPersistencePort: ApprovalRequestPersistencePort,
    private val approvalStagePersistencePort: ApprovalStagePersistencePort,
) : CreateApprovalRequestUseCase {
    @Transactional
    override fun invoke(command: CreateApprovalRequestCommand) {
        val approvalRequest = approvalRequestPersistencePort.save(command)
        val createApprovalStageCommand = createApprovalStageCommand(approvalRequest)
        approvalStagePersistencePort.save(createApprovalStageCommand)
    }

    private fun createApprovalStageCommand(approvalRequest: ApprovalRequest): CreateApprovalStageRequestCommand = CreateApprovalStageRequestCommand(
        approvalRequestId = approvalRequest.id!!,
        orderNo = 1,
        status = ApprovalStageStatus.REVIEWING,
        requireCount = 1,
    )
}
