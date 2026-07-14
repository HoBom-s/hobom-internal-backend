package com.hobom.hobominternal.adapter.inbound.rest.notion

import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.domain.notion.port.inbound.GetNotionArticleBySlugUseCase
import com.hobom.hobominternal.shared.response.HttpResponse
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
class GetNotionArticleBySlugController(
    private val getNotionArticleBySlugUseCase: GetNotionArticleBySlugUseCase,
) {
    @Operation(summary = "Get HoBom Tech Blog Contents by slug", description = "Detail contents by slug")
    @GetMapping("/hobom/tech/articles/{slug}")
    fun findBySlug(
        @PathVariable("slug") slug: String,
    ): ResponseEntity<HttpResponse<BlockChildrenResponse>> {
        val result = getNotionArticleBySlugUseCase.invoke(slug)

        return ResponseEntity.ok(HttpResponse.success(BlockChildrenResponse.from(result)))
    }
}
