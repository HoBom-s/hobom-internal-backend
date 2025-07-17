package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.dto.log.HoBomLogRequestCountResponse
import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.port.inbound.log.CountRequestHoBomLogGroupUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class CountRequestHoBomLogGroupController(
    private val countRequestHoBomLogGroupUseCase: CountRequestHoBomLogGroupUseCase,
) {
    @Operation(summary = "Count status", description = "Count status list")
    @GetMapping("/logs/request-summary")
    fun countRequest(): ResponseEntity<List<HoBomLogRequestCountResponse>> {
        val queryResult = countRequestHoBomLogGroupUseCase.invoke()
        val response = queryResult.map { HoBomLogRequestCountResponse(it.minute, it.totalRequests) }

        return ResponseEntity.ok(response)
    }
}
