package com.hobom.hobominternal.adapter.inbound.rest.notion

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionBlockUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Tech Blog", description = "HoBom Tech Blog")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class GetNotionBlockController(
    private val getNotionBlockUseCase: GetNotionBlockUseCase,
) {
    @Operation(summary = "Get HoBom Tech Blog Contents", description = "Detail contents")
    @GetMapping("/hobom/tech/{pageId}")
    fun findById(
        @PathVariable("pageId") id: String,
    ): ResponseEntity<BlockChildrenResponse> {
        val blockChildrenString = getNotionBlockUseCase.invoke(id)

        return ResponseEntity.ok(BlockChildrenResponse.from(blockChildrenString))
    }
}
