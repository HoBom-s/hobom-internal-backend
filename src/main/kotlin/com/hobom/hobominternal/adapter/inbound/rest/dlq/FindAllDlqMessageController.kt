package com.hobom.hobominternal.adapter.inbound.rest.dlq

import com.hobom.hobominternal.adapter.inbound.dto.dlq.DlqMessageResponse
import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.port.inbound.dlq.FindAllDlqMessageUseCase
import com.hobom.hobominternal.shared.page.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "DLQ", description = "HoBom System DLQ")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class FindAllDlqMessageController(
    private val findAllDlqMessageUseCase: FindAllDlqMessageUseCase,
) {
    @Operation(summary = "Find all DLQ logs", description = "With pagination ( id desc )")
    @GetMapping("/dlqs")
    fun findAll(
        @RequestParam("page") page: Int = 0,
        @RequestParam("size") size: Int = 20,
    ): ResponseEntity<PageResponse<DlqMessageResponse>> {
        val queryResult = findAllDlqMessageUseCase.invoke(page, size)
        val response = PageResponse(
            page = page,
            size = size,
            total = queryResult.total,
            content = queryResult.items.map { DlqMessageResponse.from(it) },
        )

        return ResponseEntity.ok(response)
    }
}
