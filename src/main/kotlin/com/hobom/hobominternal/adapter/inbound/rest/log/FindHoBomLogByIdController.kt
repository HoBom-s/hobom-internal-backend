package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.log.model.HoBomLogId
import com.hobom.hobominternal.domain.log.port.inbound.FindHoBomLogByIdUseCase
import com.hobom.hobominternal.shared.response.HttpResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class FindHoBomLogByIdController(
    private val findHoBomLogByIdUseCase: FindHoBomLogByIdUseCase,
) {
    @Operation(summary = "Find log by id", description = "Find single log entry")
    @GetMapping("/logs/{id}")
    fun findById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<HttpResponse<HoBomLogSearchResponse>> {
        val queryResult = findHoBomLogByIdUseCase.invoke(HoBomLogId(id))

        return ResponseEntity.ok(HttpResponse.success(HoBomLogSearchResponse.from(queryResult)))
    }
}
