package com.hobom.hobominternal.application.command.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalStepMode

data class CreateApprovalRequestCommand(
    val title: String,
    val requesterId: String,
    val resource: ResourceCommand,
    val stages: List<StageCommand>,
)

data class ResourceCommand(
    val type: String,
    val id: String,
    val version: Int?,
)

data class StageCommand(
    val order: Int,
    val mode: ApprovalStepMode,
    val steps: List<StepCommand>,
)

data class StepCommand(
    val approverIds: Set<String>,
)
