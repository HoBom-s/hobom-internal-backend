package com.hobom.hobominternal.adapter.inbound.rest.notion

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionArticlesUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Tech Blog", description = "HoBom Tech Blog")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class GetNotionArticlesController(
    private val getNotionArticlesUseCase: GetNotionArticlesUseCase,
) {
    @Operation(summary = "Get HoBom Tech Blog Articles", description = "Articles")
    @GetMapping("/hobom/tech/articles")
    fun findAll(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) cursor: String?,
    ): ResponseEntity<ArticleResponse> {
        val databaseQueryResponse = getNotionArticlesUseCase.invoke(pageSize, cursor)
        val response = ArticleResponse.from(databaseQueryResponse)

        return ResponseEntity.ok(response)
    }
}
