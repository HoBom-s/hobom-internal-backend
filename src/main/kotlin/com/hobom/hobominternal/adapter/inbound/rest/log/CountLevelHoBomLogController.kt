package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.port.inbound.CountLevelHoBomLogUseCase
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
class CountLevelHoBomLogController(
    private val countLevelHoBomLogUseCase: CountLevelHoBomLogUseCase,
) {
    @Operation(summary = "Log level distribution", description = "Count by log level")
    @GetMapping("/logs/level-summary")
    fun countLevel(
        @Parameter(description = "Time window in hours (1-168)")
        @RequestParam(defaultValue = "24")
        @Min(1)
        @Max(168) hours: Int,
    ): ResponseEntity<HttpResponse<List<HoBomLogLevelCountResponse>>> {
        val response = countLevelHoBomLogUseCase.invoke(hours)
        return ResponseEntity.ok(
            HttpResponse.success(response.map { HoBomLogLevelCountResponse(it.level, it.count) }),
        )
    }
}
