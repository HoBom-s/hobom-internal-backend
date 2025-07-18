package com.hobom.hobominternal.adapter.inbound.rest.dlq

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.port.inbound.ManualSendMessageDlqUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "DLQ", description = "HoBom System DLQ")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class ManualSendDlqMessageController(
    private val manualSendMessageDlqUseCase: ManualSendMessageDlqUseCase,
) {
    @Operation(summary = "Manual DLQ recovery", description = "Manual message send")
    @PostMapping("/dlqs/{id}/retry")
    fun manualSend(
        @PathVariable("id") id: Long,
        @RequestBody request: ManualSendDlqMessageRequest,
    ): ResponseEntity<Unit> {
        val dlqMessageId = DlqMessageId(id)
        manualSendMessageDlqUseCase.invoke(request.toCommand(dlqMessageId))

        return ResponseEntity.noContent().build()
    }
}
