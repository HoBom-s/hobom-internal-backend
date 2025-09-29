package com.hobom.hobominternal.adapter.inbound.rest.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Create Approval Request DTO")
data class CreateApprovalRequest(
    @Schema(description = "Title")
    @field:NotNull
    val title: String,

    @Schema(description = "Content")
    @field:NotNull
    val content: String,

    @Schema(description = "Requester ID")
    @field:NotNull
    val requesterId: String,
) {
    fun toCommand(): CreateApprovalRequestCommand = CreateApprovalRequestCommand(
        title = title,
        content = content,
        requesterId = requesterId,
    )
}
