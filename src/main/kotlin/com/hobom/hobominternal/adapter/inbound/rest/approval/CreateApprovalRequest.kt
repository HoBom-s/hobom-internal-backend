package com.hobom.hobominternal.adapter.inbound.rest.approval

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand
import com.hobom.hobominternal.application.command.approval.ResourceCommand
import com.hobom.hobominternal.application.command.approval.StageCommand
import com.hobom.hobominternal.application.command.approval.StepCommand
import com.hobom.hobominternal.domain.approval.model.ApprovalStepMode
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(
    name = "CreateApprovalRequest",
    description = "승인요청 생성 바디. Stage/Step 스펙 전달.",
)
data class CreateApprovalRequest(
    @field:NotBlank
    @field:Size(max = 50)
    @Schema(
        description = "승인요청 제목",
        maxLength = 50,
    )
    val title: String,

    @field:NotBlank
    @Schema(
        description = "요청한 사람의 ID",
    )
    val requesterId: String,

    @field:NotNull
    @Schema(description = "원본 리소스 참조(승인 결과를 반영할 대상). 필요 없으면 별도의 독립 승인으로 처리 가능.")
    val resource: Resource,

    @field:NotEmpty
    @field:Size(min = 1, max = 10)
    @Schema(description = "승인 차수(Stage) 목록. order 오름차순으로 해석.")
    val stages: List<Stage>,
) {
    @Schema(
        name = "Resource",
        description = "승인요청이 참조하는 비즈니스 리소스(타입/ID/버전)",
    )
    data class Resource(
        @field:NotBlank
        @field:Size(max = 64)
        @Schema(
            description = "리소스 타입 (예: WISH_LIST etc...)",
            example = "WISH_LIST",
        )
        val type: String,

        @field:NotBlank
        @field:Size(max = 128)
        @Schema(
            description = "리소스 ID",
            example = "hobom-wish-123",
        )
        val id: String,

        @field:Min(0)
        @Schema(
            description = "리소스 버전(재승인/동시성 제어용). 모르면 0 또는 null.",
            example = "7",
            nullable = true,
        )
        val version: Int?,
    )

    @Schema(
        name = "Stage",
        description = "승인의 한 차수. mode에 따라 스텝 승인 조건이 달라짐.",
    )
    data class Stage(
        @field:Min(1)
        @Schema(description = "차수 순서(1부터 시작, 오름차순 정렬 가정)")
        val order: Int,

        @Schema(
            description = "차수의 승인 모드: ALL(모든 스텝 충족) / ANY(어느 한 스텝 충족)",
            allowableValues = ["ALL", "ANY"],
        )
        val mode: ApprovalStepMode,

        @field:NotEmpty
        @field:Size(min = 1, max = 10)
        @Schema(description = "해당 차수의 병렬 스텝 목록")
        val steps: List<Step>,
    )

    @Schema(
        name = "Step",
        description = "차수 내 병렬 승인 단위. approverIds 중 규칙에 따라 승인되면 스텝 완료.",
    )
    data class Step(
        @field:NotEmpty
        @field:Size(min = 1, max = 10)
        @Schema(
            description = "이 스텝을 승인할 수 있는 사용자 ID 집합",
        )
        val approverIds: Set<String>,
    )
}

fun CreateApprovalRequest.toCommand(): CreateApprovalRequestCommand = CreateApprovalRequestCommand(
    title = title,
    requesterId = requesterId,
    resource = ResourceCommand(
        type = resource.type,
        id = resource.id,
        version = resource.version,
    ),
    stages = stages.map {
        StageCommand(
            order = it.order,
            mode = it.mode,
            steps = it.steps.map { step ->
                StepCommand(step.approverIds)
            },
        )
    },
)
