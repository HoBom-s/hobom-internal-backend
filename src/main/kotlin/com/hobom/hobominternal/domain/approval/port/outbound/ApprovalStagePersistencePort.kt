package com.hobom.hobominternal.domain.approval.port.outbound

import com.hobom.hobominternal.application.command.approval.CreateApprovalStageRequestCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalStage
import com.hobom.hobominternal.domain.approval.model.ApprovalStageId

interface ApprovalStagePersistencePort {
    fun load(id: ApprovalStageId): ApprovalStage

    fun save(command: CreateApprovalStageRequestCommand)
}
