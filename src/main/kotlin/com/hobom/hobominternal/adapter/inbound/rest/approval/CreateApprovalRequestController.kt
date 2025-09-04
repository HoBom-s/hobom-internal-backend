package com.hobom.hobominternal.adapter.inbound.rest.approval

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.approval.port.inbound.CreateApprovalRequestUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Approval Request", description = "HoBom System Approval Request")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class CreateApprovalRequestController(
    private val createApprovalRequestUseCase: CreateApprovalRequestUseCase,
) {
    @Operation(summary = "Create new approval request", description = "New approval request")
    @PostMapping("/approval-request")
    fun createNewApprovalRequest(
        @RequestHeader("idempotency-key") idemKey: String,
        @RequestBody request: CreateApprovalRequest,
    ): ResponseEntity<Unit> {
        require(idemKey.isNotBlank()) { "Idempotency key required!" }

        createApprovalRequestUseCase.invoke(request.toCommand(), idemKey)

        return ResponseEntity.noContent().build()
    }
}
