package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.port.inbound.CountRequestHoBomLogGroupUseCase
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
class CountRequestHoBomLogGroupController(
    private val countRequestHoBomLogGroupUseCase: CountRequestHoBomLogGroupUseCase,
) {
    @Operation(summary = "Request volume by minute", description = "Requests grouped by minute")
    @GetMapping("/logs/request-summary")
    fun countRequest(
        @Parameter(description = "Time window in hours (1-168)")
        @RequestParam(defaultValue = "24")
        @Min(1)
        @Max(168) hours: Int,
    ): ResponseEntity<HttpResponse<List<HoBomLogRequestCountResponse>>> {
        val queryResult = countRequestHoBomLogGroupUseCase.invoke(hours)
        val response = queryResult.map { HoBomLogRequestCountResponse(it.minute, it.totalRequests) }

        return ResponseEntity.ok(HttpResponse.success(response))
    }
}
