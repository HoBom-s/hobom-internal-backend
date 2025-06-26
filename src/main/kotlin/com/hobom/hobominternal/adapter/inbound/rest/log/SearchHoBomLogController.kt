package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.dto.log.HoBomLogSearchRequest
import com.hobom.hobominternal.adapter.inbound.dto.log.HoBomLogSearchResponse
import com.hobom.hobominternal.adapter.inbound.dto.log.toCriteria
import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.port.inbound.log.SearchHoBomLogUseCase
import com.hobom.hobominternal.shared.page.PageRequest
import com.hobom.hobominternal.shared.page.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class SearchHoBomLogController(
    private val searchHoBomLogUseCase: SearchHoBomLogUseCase,
) {
    @Operation(summary = "Filter logs", description = "With pagination")
    @GetMapping("/logs")
    fun search(
        @ParameterObject
        request: HoBomLogSearchRequest,
        @ParameterObject
        pageRequest: PageRequest,
    ): ResponseEntity<PageResponse<HoBomLogSearchResponse>> {
        val queryResult = searchHoBomLogUseCase.invoke(request.toCriteria(), pageRequest.page, pageRequest.size)
        val response = PageResponse(
            page = pageRequest.page,
            size = pageRequest.size,
            total = queryResult.total,
            content = queryResult.items.map { HoBomLogSearchResponse.from(it) },
        )

        return ResponseEntity.ok(response)
    }
}
