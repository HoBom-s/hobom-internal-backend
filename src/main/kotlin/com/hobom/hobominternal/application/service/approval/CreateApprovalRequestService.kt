package com.hobom.hobominternal.application.service.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
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
        approvalRequestPersistencePort.save(command)
    }
}
