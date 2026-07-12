package com.hobom.hobominternal.adapter.inbound.rest.dlq

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.dlq.model.DlqMessageId
import com.hobom.hobominternal.domain.dlq.port.inbound.FindDlqMessageByIdUseCase
import com.hobom.hobominternal.shared.response.HttpResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "DLQ", description = "HoBom System DLQ")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class FindDlqMessageByIdController(
    private val findDlqMessageByIdUseCase: FindDlqMessageByIdUseCase,
) {
    @Operation(summary = "Find DLQ log", description = "Find by id")
    @GetMapping("/dlqs/{id}")
    fun findById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<HttpResponse<DlqMessageResponse>> {
        val response = findDlqMessageByIdUseCase.invoke(DlqMessageId(id))

        return ResponseEntity.ok(HttpResponse.success(DlqMessageResponse.from(response)))
    }
}
