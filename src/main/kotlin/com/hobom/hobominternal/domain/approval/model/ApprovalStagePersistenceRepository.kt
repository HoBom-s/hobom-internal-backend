package com.hobom.hobominternal.domain.approval.model

interface ApprovalStagePersistenceRepository {
    fun load(id: ApprovalStageId): ApprovalStage

    fun save(approvalStage: ApprovalStage)
}
