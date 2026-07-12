package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.port.inbound.SearchHoBomLogUseCase
import com.hobom.hobominternal.shared.page.PageRequest
import com.hobom.hobominternal.shared.response.HttpResponse
import com.hobom.hobominternal.shared.response.PaginatedItems
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
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
        @Valid @ParameterObject
        pageRequest: PageRequest,
    ): ResponseEntity<HttpResponse<PaginatedItems<HoBomLogSearchResponse>>> {
        val queryResult = searchHoBomLogUseCase.invoke(request.toCriteria(), pageRequest.page, pageRequest.size)
        val paginated = PaginatedItems(
            items = queryResult.items.map { HoBomLogSearchResponse.from(it) },
            totalCount = queryResult.total,
            offset = pageRequest.page * pageRequest.size,
            limit = pageRequest.size,
        )

        return ResponseEntity.ok(HttpResponse.success(paginated))
    }
}
