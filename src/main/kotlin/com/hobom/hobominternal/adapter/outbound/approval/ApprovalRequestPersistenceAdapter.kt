package com.hobom.hobominternal.adapter.outbound.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestPersistenceRepository
import com.hobom.hobominternal.domain.approval.port.outbound.ApprovalRequestPersistencePort
import org.springframework.stereotype.Component

@Component
class ApprovalRequestPersistenceAdapter(
    private val approvalRequestPersistenceRepository: ApprovalRequestPersistenceRepository,
) : ApprovalRequestPersistencePort {
    override fun load(id: ApprovalRequestId): ApprovalRequest {
        return approvalRequestPersistenceRepository.load(id)
    }

    override fun save(command: CreateApprovalRequestCommand): ApprovalRequest {
        return approvalRequestPersistenceRepository.save(command.toSaveModel())
    }
}
