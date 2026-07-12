package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.port.inbound.TopErrorEndpointHoBomLogUseCase
import com.hobom.hobominternal.shared.response.HttpResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class TopErrorEndpointHoBomLogController(
    private val topErrorEndpointHoBomLogUseCase: TopErrorEndpointHoBomLogUseCase,
) {
    @Operation(summary = "Top error endpoints", description = "Endpoints with most errors")
    @GetMapping("/logs/endpoint-errors")
    fun topErrors(
        @Parameter(description = "Time window in hours (1-168)")
        @RequestParam(defaultValue = "24")
        @Min(1)
        @Max(168) hours: Int,
        @Parameter(description = "Max results (1-50)")
        @RequestParam(defaultValue = "20")
        @Min(1)
        @Max(50) limit: Int,
    ): ResponseEntity<HttpResponse<List<HoBomLogEndpointErrorResponse>>> {
        val result = topErrorEndpointHoBomLogUseCase.invoke(hours, limit)
        val response = result.map {
            HoBomLogEndpointErrorResponse(
                path = it.path,
                httpMethod = it.httpMethod,
                totalCount = it.totalCount,
                errorCount = it.errorCount,
                errorRate = if (it.totalCount > 0) it.errorCount.toDouble() / it.totalCount else 0.0,
            )
        }

        return ResponseEntity.ok(HttpResponse.success(response))
    }
}
