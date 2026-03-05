package com.hobom.hobominternal.adapter.inbound.rest.dlq

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.dlq.port.inbound.FindAllDlqMessageUseCase
import com.hobom.hobominternal.shared.response.HttpResponse
import com.hobom.hobominternal.shared.response.PaginatedItems
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@Tag(name = "DLQ", description = "HoBom System DLQ")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class FindAllDlqMessageController(
    private val findAllDlqMessageUseCase: FindAllDlqMessageUseCase,
) {
    @Operation(summary = "Find all DLQ logs", description = "With pagination ( id desc )")
    @GetMapping("/dlqs")
    fun findAll(
        @RequestParam("page") @Min(0) page: Int = 0,
        @RequestParam("size") @Min(1) @Max(100) size: Int = 20,
    ): ResponseEntity<HttpResponse<PaginatedItems<DlqMessageResponse>>> {
        val queryResult = findAllDlqMessageUseCase.invoke(page, size)
        val paginated = PaginatedItems(
            items = queryResult.items.map { DlqMessageResponse.from(it) },
            totalCount = queryResult.total,
            offset = page * size,
            limit = size,
        )

        return ResponseEntity.ok(HttpResponse.success(paginated))
    }
}
