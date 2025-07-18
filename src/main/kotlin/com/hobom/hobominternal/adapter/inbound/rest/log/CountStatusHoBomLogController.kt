package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.port.inbound.CountStatusHoBomLogUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class CountStatusHoBomLogController(
    private val countStatusHoBomLogUseCase: CountStatusHoBomLogUseCase,
) {
    @Operation(summary = "Count status", description = "Count status list")
    @GetMapping("/logs/status-summary")
    fun countStatus(): ResponseEntity<List<HoBomLogStatusCountResponse>> {
        val response = countStatusHoBomLogUseCase.invoke()
        return ResponseEntity.ok(response.map { HoBomLogStatusCountResponse(it.statusCode, it.count) })
    }
}
