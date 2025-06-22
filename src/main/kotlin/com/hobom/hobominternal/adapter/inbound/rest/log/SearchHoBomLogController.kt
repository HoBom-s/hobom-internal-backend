package com.hobom.hobominternal.adapter.inbound.rest.log

import com.hobom.hobominternal.adapter.inbound.dto.log.HoBomLogSearchRequest
import com.hobom.hobominternal.adapter.inbound.dto.log.HoBomLogSearchResponse
import com.hobom.hobominternal.adapter.inbound.dto.log.toCriteria
import com.hobom.hobominternal.adapter.inbound.prefix.HOBOM_INTERNAL_END_POINT_PREFIX
import com.hobom.hobominternal.port.inbound.log.SearchHoBomLogUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "HoBom Logs", description = "HoBom Logs")
@RestController
@RequestMapping(HOBOM_INTERNAL_END_POINT_PREFIX)
class SearchHoBomLogController(
    private val searchHoBomLogUseCase: SearchHoBomLogUseCase,
) {
    @Operation(summary = "로그 목록 검색", description = "검색 조건과 페이징 정보를 기반으로 로그 목록을 조회합니다.")
    @GetMapping("/logs")
    fun search(
        @ParameterObject
        request: HoBomLogSearchRequest,
        @ParameterObject
        pageable: Pageable,
    ): ResponseEntity<Page<HoBomLogSearchResponse>> {
        val queryResult = searchHoBomLogUseCase.invoke(request.toCriteria(), pageable)
        val response = queryResult.map { HoBomLogSearchResponse.from(it) }

        return ResponseEntity.ok(response)
    }
}
